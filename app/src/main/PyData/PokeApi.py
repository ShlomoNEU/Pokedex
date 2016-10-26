import json
import requests
import time

baseURL = "http://pokeapi.co/api/v2/pokemon/"
index = 503
maxcount = 721

data = {}
while index <= maxcount:
    print 'I\'m Awake'
    PokemonResp = requests.get(baseURL + str(index))
    if PokemonResp.status_code == 429:
        print (PokemonResp.headers)
        time.sleep(float(PokemonResp.headers['Retry-After']))
    elif PokemonResp.status_code != 200:
        print ('Error Getting Main Api', baseURL + str(index), 'Code', PokemonResp.status_code)

    print "Done Pokemon Base Data from Server"

    SpeciesResp = requests.get("http://pokeapi.co/api/v2/pokemon-species/" + str(index))
    if SpeciesResp.status_code == 429:
        print (SpeciesResp.headers)
        time.sleep(float(SpeciesResp.headers['Retry-After']))
    elif SpeciesResp.status_code != 200:
        print ('Error Getting pokemon-species ', index, 'Chain CodeStatus', SpeciesResp.status_code)
    print ('Done Species Data from Server')

    PokemonJsonDat = PokemonResp.json()
    SpeciesJsonDat = SpeciesResp.json()
    EvoResponse = requests.get(str(SpeciesJsonDat['evolution_chain']['url']))
    if EvoResponse.status_code == 429:
        print (SpeciesResp.headers)
        time.sleep(float(SpeciesResp.headers['Retry-After']))
    elif EvoResponse.status_code != 200:
        print ('Error Getting pokemon-species ', index, 'Chain CodeStatus', SpeciesResp.status_code)
    print ('Done With Chain Evoltion')

    EvoJsonDat = EvoResponse.json();
    del EvoResponse, SpeciesResp, PokemonResp

    # get Type
    print "Get type"
    tmp = PokemonJsonDat['types']
    type = {}
    if len(tmp) < 2:
        dat = json.loads(json.dumps(tmp[0]))
        type[1] = dat["type"]['name']
        type[2] = ""
        del dat
    else:
        dat = json.loads(json.dumps(tmp[0]))
        type[1] = dat["type"]['name']
        dat = json.loads(json.dumps(tmp[1]))
        type[2] = dat["type"]['name']
        del dat
    # Done get Type
    pokemon = {'id': PokemonJsonDat['id'], 'name': PokemonJsonDat['name'],
               'weight': PokemonJsonDat['weight'], 'height': PokemonJsonDat['height']}
    pokemon["type"] = type
    del tmp, type
    # Get varieties
    print "Get varieties"
    varieties = {}
    Mvarieties = {}
    varIndex = 0
    for variety in SpeciesJsonDat['varieties']:
        varieties = {}
        varieties['name'] = variety['pokemon']['name']
        varieties['url'] = variety['pokemon']['url']
        Mvarieties[varIndex] = varieties
        varIndex += 1
    pokemon['varieties'] = Mvarieties

    del varieties, Mvarieties, varIndex, variety
    # End varieties

    # Getting Evoltion
    # Base Evolotion
    print "Get Base Evo"
    Evoltion_details = {}
    Evoltion = {}
    Evoltion['name'] = EvoJsonDat['chain']['species']['name'] + "@URL" + \
                       EvoJsonDat['chain']['species']['url']
    Evoltion_details['0'] = Evoltion

    # Extra Evoltions
    print "Get Extra Evo"
    EvoIndex1 = 0
    EvoIndex2 = 0
    Evoltion = {}
    Evoltion2 = {}
    tmp = {}
    tmp2 = {}
    Evo = 0
    Evo2 = 0
    for Evo in EvoJsonDat['chain']['evolves_to']:
        Evoltion = {}
        Evoltion['name'] = Evo['species']['name'] + "@URL" + Evo['species']['url']
        Evoltion['evolution_details'] = Evo['evolution_details']
        tmp[str(EvoIndex1)] = Evoltion
        for Evo2 in Evo['evolves_to']:
            Evoltion2['name'] = Evo2['species']['name'] + "@URL" + Evo2['species']['url']
            Evoltion2['evolution_details'] = Evo2['evolution_details']
            tmp2[str(EvoIndex2)] = Evoltion2
            EvoIndex2 += 1
        EvoIndex1 += 1
    if EvoIndex1 >= 1:
        Evoltion_details['1'] = tmp
    if EvoIndex2 >= 1:
        Evoltion_details['2'] = tmp2
    pokemon['Evoltion'] = Evoltion_details
    del EvoIndex1, EvoIndex2, Evoltion, Evoltion2, tmp, tmp2, Evo2, Evo, Evoltion_details

    # End Evolotion

    # Generation First Apperance
    print "Get Generation"
    pokemon['Gen'] = SpeciesJsonDat['generation']['name']

    # End Generation First Apperance

    # Getting Egg Group
    print "Get Egg Group"
    MEggGroups = {}
    tmpIndex = 0
    for eggGroup in SpeciesJsonDat['egg_groups']:
        MEggGroups[tmpIndex] = {'name': eggGroup['name']}
        tmpIndex += 1
    pokemon['Egg Group'] = MEggGroups
    del eggGroup, tmpIndex, MEggGroups

    # End Of EggGroup

    # Strat flavor text entries
    print "Get Flavor text"
    flavor = {}
    tmpIndex = 0
    for Ftext in SpeciesJsonDat['flavor_text_entries']:
        if Ftext['language']['name'] == "en":
            flavor[tmpIndex] = {'game version': Ftext['version']['name'],
                                "Text": Ftext['flavor_text']}
            tmpIndex += 1
    pokemon['Flavor Text'] = flavor
    # End flavor text entries


    # Saving to file
    print "saving file"
    with open('Json/ID' + str(index) + '.txt', "w") as f:
        f.write(json.dumps(pokemon))
        f.close()
    print ('Downloaded-> ' + pokemon['name'] + ' index: ' + str(index))
    index += 1
    print ('Going to Sleep')
    time.sleep(2)

exit()
