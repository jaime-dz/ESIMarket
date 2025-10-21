from bottle import run, route, response, request
import json


def dni_valido(dni):
    letras = "TRWAGMYFPDXBNJZSQVHLCKE"

    # Validar formato básico: 8 números + 1 letra
    if len(dni) != 9 or not dni[:-1].isdigit() or not dni[-1].isalpha():
        return False

    numero = int(dni[:-1])
    letra = dni[-1].upper()

    letra_correcta = letras[numero % 23]

    return letra == letra_correcta


#################################################################################################################3

@route('/personal',method='GET')
def listar_personal():

    response.content_type = 'application/json'
    return json.dumps(personalUCA)


@route('/personal/categoria/<cat>',method='GET')
def listar_personal_categoria( cat ):

    lista = dict()

    for key,value in personalUCA.items():
        if personalUCA[key][3] == cat :
            lista[key] = value

    response.content_type = 'application/json'
    return json.dumps(lista)


@route('/personal/nombre/<nom>',method='GET')
def listar_personal_nombre(nom):
    nom = nom.lower()
    resultados = {}

    for dni, datos in personalUCA.items():
        nombre = datos[0]
        if nombre.lower().startswith(nom.lower()):
            resultados[dni] = datos

    response.content_type = 'application/json'
    return json.dumps(resultados)


@route('/personal/dni/<dni>',method='GET')
def listar_personal_dni(dni):

    lista = dict()

    for key,value in personalUCA.items():
        if key == dni:
            lista[key] = value

    response.content_type = 'application/json'
    return json.dumps(lista)


@route('/personal/asignatura/<asig>',method='GET')
def listar_personal_asig(asig):

    lista = dict()

    for key,value in personalUCA.items():
        if  value[3] == "PDI" and asig in value[4]:
            lista[key] = value


    response.content_type = 'application/json'
    return json.dumps(lista)


@route('/personal/baja/<dni>',method='DELETE')
def baja_personal(dni):

    for key in personalUCA.keys():
        if dni in key:
            personalUCA.pop(key)
            return {'success': 'Miembro eliminado correctamente'}
        else:
            response.status = 400
            return {'error': 'DNI no valido'}


@route('/personal/alta',method='POST')
def alta_personal():

    data = request.json

    if not data or len(data) != 1:
        response.status = 400
        return {'error': 'Formato de datos incorrecto'}


    for key in data.keys():

        if not dni_valido(key):
            response.status = 400
            return {'error': 'DNI no valido'}

        if len(data[key]) < 4:
            response.status = 400
            return {'error': 'Cantidad de datos incorrecto' }

        if ( data[key][3] not in ["PDI","PAS","Becario"] ):
            response.status = 400
            return {'error': 'Formato de categoria incorrecto' }

        elif ( data[key][3] == "PDI" ):
            tama = 5
        else:
            tama = 4

        if len(data[key]) > tama:
            response.status = 400
            return {'error': 'Cantidad de datos incorrecto' }

        personalUCA[key] = data[key]

        with open('personalUCA.json', 'w', encoding='utf-8') as f:
            json.dump(personalUCA, f, ensure_ascii=False, indent=4)

        response.content_type = 'application/json'
        return json.dumps({ key : data[key] } )



@route('/personal/mod/<dni>',method='PUT')
def mod_personal( dni ):

    if dni not in personalUCA.keys():
        response.status = 400
        return {'error': 'DNI no encontrado' }

    data = request.json

    if len(data) < 4:
         response.status = 400
         return {'error': 'Cantidad de datos incorrecto' }

    if ( data[3] not in ["PDI","PAS","Becario"] ):
         response.status = 400
         return {'error': 'Formato de categoria incorrecto' }
    else:
        if ( data[3] == "PDI" ):
            tama = 5
        else:
            tama = 4

        if len(data) > tama:
            response.status = 400
            return {'error': 'Cantidad de datos incorrecto' }


    personalUCA[dni] = data
    response.content_type = 'application/json'
    return json.dumps({ dni : data } )




if __name__ == '__main__':
    try:
        with open('personalUCA.json', 'r', encoding='utf-8') as f:
            contenido = f.read().strip()
            if contenido:
                personalUCA = json.loads(contenido)
            else:
                personalUCA = {}
    except (FileNotFoundError, json.JSONDecodeError):
        personalUCA = {}

    run(host='0.0.0.0', port=4444, debug=True)
