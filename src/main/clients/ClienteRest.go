package main

import (
	"encoding/json"
	"fmt"
	"github.com/go-resty/resty"
	"os"
	"os/exec"
	"time"
	"unicode"
)

var DomainURL string = "http://localhost:42069"
var token string

type urlCorta struct {
	 Id 	   int    `json:"id"`
	 Creador   string `json:"creador"`
	 Url_orig  string `json:"url_orig"`
	 Fecha     string `json:"fecha"`
	 Stats	   stats  `json:"stats"`

}
type stats struct{
	ActPerHour map[string]interface{} `json:"ActPerHour"`
	Browsers map[string]interface{} `json:"Browsers"`
	OSs map[string]interface{} `json:"OSs"`
}

func retrieveToken(usuario string, pass string) (res bool) {
	client := resty.New()
	resp, err := client.R().
		EnableTrace().
		SetHeader("Content-Type", "application/json").
		SetQueryParam("usuario",usuario).
		SetQueryParam("pass",pass).
		Post(DomainURL+"/token")
	var result map[string]interface{}
	json.Unmarshal(resp.Body(),&result)
	token = result["token"].(string)
	println(token)
	//println(string(resp.Body()))
	if err != nil {
		res = false
	} else {
		res = true
	}
	return
}
func getAllURLs() []urlCorta {
	client := resty.New()
	var urls []urlCorta
	resp, _ := client.R().
		EnableTrace().
		SetHeader("token",token).
		Get(DomainURL+"/rest/urls")
	body := resp.Body()
	json.Unmarshal(body, &urls)
	return urls
}

func getUserURLs(username string) []urlCorta{
	client := resty.New()
	var urls []urlCorta
	resp, _ := client.R().
		EnableTrace().
		SetHeader("token",token).
		Get(DomainURL+"/rest/urls/" + username)

	json.Unmarshal(resp.Body(), &urls)
	return urls
}

func crearURL(newUrl urlCorta) (res bool) {
	client := resty.New()
	resp, err := client.R().
		EnableTrace().
		SetHeader("Content-Type", "application/json").
		SetHeader("token",token).
		SetBody(newUrl).
		Post(DomainURL+"/rest/newUrl")
	println(string(resp.Body()))
	if err != nil {
		res = false
	} else {
		res = true
	}
	return
}


func clearMenu() {
	clear := make(map[string]func()) //Initialize it
	clear["linux"] = func() {
		cmd := exec.Command("clear") //Linux example, its tested
		cmd.Stdout = os.Stdout
		cmd.Run()
	}
	clear["windows"] = func() {
		cmd := exec.Command("cmd", "/c", "cls") //Windows example, its tested
		cmd.Stdout = os.Stdout
		cmd.Run()
	}
}

func initMenu() {
	clearMenu()
	fmt.Println("Bienvenido al cliente de API REST para el acortador de URL para ISC-415, digite el número necesario para lo que quiere hacer, sino digite q para salir")
	fmt.Println("0) Obtener un token de acceso")
	fmt.Println("1) Ver todas las URL")
	fmt.Println("2) Ver las URL pertenecientes a un usuario")
	fmt.Println("3) Crear un nuevo link")
}

func main() {
	doIt := true
	var i rune
	for doIt {
		initMenu()
		_, _ = fmt.Scanf("%c\n", &i)
		switch i {
		case '1':
			if(token!=""){
				urls := getAllURLs()
				for _, url := range urls {
					fmt.Printf("%+v\n",url)
				}
			}else{
				println("No tiene un token habilitado")
			}



		case '2':
			if(token!=""){
				print("Digite el numero de matricula del estudiante")
				var user string
				_, _ = fmt.Scanf("%s\n", &user)
				urls := getUserURLs(user)
				for _, url := range urls {
					fmt.Printf("%+v\n",url)
				}
			}else{
				println("No tiene un token habilitado")
			}


		case '3':
			if(token!=""){
				var newurl urlCorta
				print("Digite el nombre del creador ")
				_, _ = fmt.Scanf("%s\n", &(newurl.Creador))
				print("Digite la URL ")
				_, _ = fmt.Scanf("%s\n", &(newurl.Url_orig))
				println("Fecha de insercion: ")
				newurl.Fecha = time.Now().UTC().Format("2006-01-02T15:04:05-0700")
				print(newurl.Fecha)
				if crearURL(newurl) {
					println("operacion exitosa")
				} else {
					println("ERROR")
				}
			}else{
				println("No tiene un token habilitado")
			}
		case 'q':
			doIt = false
		case '0':
			var usr string
			var pass string
			println("Digite el usuario: ")
			_, _ = fmt.Scanf("%s\n", &usr)
			println("Digite su contraseña: ")
			_, _ = fmt.Scanf("%s\n", &pass)
			retrieveToken(usr,pass)
		default:
			println("Digite una opcion valida e intentelo de nuevo (pulse cualquier tecla para continuar)")
			//_, _ = fmt.Scanf("%c", &i)
		}
		correct := false
		for !correct {
			if doIt {
				println("Desea hacer otra operación? [Y/n]")
				var inp rune
				_, _ = fmt.Scanf("%c\n", &inp)
				if unicode.ToUpper(inp) == 'N' {
					doIt = false
					correct = true
				} else if unicode.ToUpper(inp) != 'Y' {
					println("Digite una opcion valida")
				} else {
					correct = true
				}
			}
		}

	}

	/*estudiantes := getEstudiantes()
	for _, est := range estudiantes {
		println("nombre: " + est.Nombre)
		println("matricula: " + strconv.Itoa(est.Matricula))
		println("correo: " + est.Correo)
		println("carrera: " + est.Carrera)
	}
	mat := 20160138
	stud := getEstudiante(mat)
	println("nombre: " + stud.Nombre)
	println("matricula: " + strconv.Itoa(stud.Matricula))
	println("correo: " + stud.Correo)
	println("carrera: " + stud.Carrera)

	var newEstud estudiante
	newEstud.Matricula = 20160138
	newEstud.Nombre = "José Ureña"
	newEstud.Correo = "polquefuequemedicuenta@pablopiddy.do"
	newEstud.Carrera = "ISC"
	fmt.Println(crearEstudiante(newEstud))*/
}
