from suds.client import Client

url = 'http://localhost:7777/ws/AcademicoWebService'
wsdl = 'http://localhost:7777/ws/AcademicoWebService?wsdl'

client = Client(url=wsdl, location=url)

def listarEstudiantes():
    lista = client.service.getAllEstudiante()
    for est in lista:
        print("Matricula: {0}, Nombre: {1}".format(est['matricula'], est['nombre']))

def asignatura(id):
    return client.service.getAsignatura(id)


while True:
    opcion = int(input('''\nPresione el numero de la opcion correspondiente
    (1) Listar estudiantes
    (2) Buscar una asignatura
    (3) Buscar un profesor
    (q) Salir
    :'''))
    
    if (opcion == 1):
        listarEstudiantes()

    elif (opcion == 2):
        id = input("\Digite el codigo de la asignatura: ")
        print(asignatura(id))

    elif (opcion==3):
        id = input("\Digite el codigo del profesor: ")
        print(client.service.getProfesor(id))

    elif (opcion=='q'):
        break
    else:
        print("Digite una opcion valida")
    

    input("\nPresione cualquier tecla para continuar")