package hello

import (
	"fmt"
	"strings"
	"io/ioutil"
    "appengine"
    "appengine/datastore"
    "appengine/urlfetch"
    "net/http"
    "time"
    "html/template"
)

var templates = template.Must(template.ParseFiles("tmpl/regform.html", "tmpl/reglist.html", "tmpl/msgform.html"))

type Device struct {
	RegistrationId string
	Model string
	Date time.Time
}

func init() {
	http.HandleFunc("/regform", regform)
	http.HandleFunc("/regsave", regsave)
	http.HandleFunc("/msgform", msgform)
	http.HandleFunc("/msgsend", msgsend)
	http.HandleFunc("/delete", delete)
	http.HandleFunc("/", index)
}

func msgform(w http.ResponseWriter, r *http.Request) {
	rid := r.FormValue("RegistrationId")
	if err := templates.ExecuteTemplate(w, "msgform.html", rid); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

var payload = `
{ "data": {
    "message": "%v"
  },
  "registration_ids": ["%v"]
}`

func msgsend(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	client := urlfetch.Client(c)
	reqstring := fmt.Sprintf(payload, r.FormValue("Message"), r.FormValue("RegistrationId"))
	req, err := http.NewRequest("POST", "https://android.googleapis.com/gcm/send", strings.NewReader(reqstring))
	req.Header.Add("Authorization", "key=AIzaSyAUBMNk774FRfxbrWexGCJFnwSB17yDD6A")
	req.Header.Add("Content-Type", "application/json");
	res, err := client.Do(req)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	res.Body.Close()
	w.Write(body)
}

func delete(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	rid := r.FormValue("RegistrationId")
	key := datastore.NewKey(c, "Device", rid, 0, nil)
	if err := datastore.Delete(c, key); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	http.Redirect(w, r, "/", http.StatusFound)
}

func index(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	q := datastore.NewQuery("Device")
	devices := make([]Device, 0, 10)
	if _, err := q.GetAll(c, &devices); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if err := templates.ExecuteTemplate(w, "reglist.html", devices); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

func regform(w http.ResponseWriter, r *http.Request) {
	http.ServeFile(w, r, "tmpl/regform.html")
}

func regsave(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	d := Device{
		RegistrationId: r.FormValue("RegistrationId"),
		Model: r.FormValue("Model"), 
		Date: time.Now(),
	}
	key := datastore.NewKey(c, "Device", d.RegistrationId, 0, nil)
	datastore.Put(c, key , &d)
	http.Redirect(w, r, "/", http.StatusFound)
}
