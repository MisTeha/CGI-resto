# CGI 2026 suvepraktika proovitöö
**Autor:** Uku Mihkel Kolka  
**Teema:** Restorani broneeringusüsteem

## Rakenduse käivitamine

### Docker Compose-iga

Käivitada juurkaustas

```
docker compose up
```

Kui see töötab, siis minna brauseris aadressile http://localhost:4173. Et backendi pärida, käivitada rakendus manuaalselt (vt järgmine alapeatükk) või `docker.compose.yml` failis sisse kommenteerida pordi `8080` edastus.

### Manuaalselt (dev)

Java backend
```
sh /backend/gradlew bootRun
```

Svelte frontend
```
cd frontend/
npm run dev
```

## Backend

Olen väga tuttav veebirakendustele backendi loomisega ja tavadega, kuid Spring Boot-i õppisin süvitsi esimest korda selle projekti tegemise käigus. Arendades kasutasin võimalikult palju Spring-i dokumentatsiooni ja näiteid, projekti failistruktuur on üksnes keelemudeli pakutud (mõnel juhul parandatud). 

## Frontend

Valisin raamistikuks Svelte, kuna töötan aktiivselt veel ühe Svelte rakendusega. Seda proovitööd tehes polnud frontend minu jaoks prioriteet, seetõttu arendasin seda suuresti agendiga, tehes ise api-ga seotud funktsioone ning pidevalt kontrollides, et kood püsiks ikkagi selline, mida ise ka kirjutaks. Committides ning failisisestes kommentaarides on täpsustatud, kas tegija olin mina või agent. 



### Õppisin / tuletasin meelde / teadvustasin 

- Java Stream-id on väga (minuarust) väga kohmakad, aga väga rikkalikud ning palju kasutatud *(teadvustasin)*
- Javas (ja mujal) API-des on tihti kasutusel lisaks *handler*-ile ja *service*-ile **DTO**-d, mis map-ivad endpointi ja service vahel andmetüüpe. Neid ise aga kasutusele ei võtnud ja hõlmasin seda *service* tasemel. *(õppisin)*
- API-t luues arvestada sellega, mis frontendis toimuma hakkab, on keeruline. Pean silmas seda, et kas nt tagastada ainult laudade ID-sid või tervet objekti. (tuletasin meelde)
- Frontendis tuleb *error handling*-u jaoks arvestada juhtudega, mis pole backendis probleemid. (teadvustasin ja nt "reservation date must be in the future" pole *handle*-itud.)
- Korrapärast või ühtlast juhuslikkust ei ole kerge saavutada. (tuletasin meelde)

### Komistuskohad

- Spring-i "maagia", nii koodi kirjutades kui debugimisel, tuli oma keerukuse tõttu sama palju kahjuks, kui kasuks.
- Raske oli otsustada, milliseid filtreid rakendada andmebaasis ning milliseid *service* tasemel. Lõpuks otsustasin olla kuigi andmebaasi filtrite põhine (vt AvailabilityService).

## TODO

[x] dockeriga käivitamine
[x] svelte kliendipoolne koristus:  
[x] - svelte v5-pärane kood  
[x] - komponendid Daisy ja shadcn kasutusel, mitte custom gpt kood  
[x] suvaliste reserveeringute gen db loomisel  
[x] ~~hoida teste up to date, (pole prioriteet)~~   
[x] frontend'i loogika (serveri poole) refactor 
[x] alustada frontendiga  
[x] laudade scoring valem / midagi muud (aga võiks veel muutuda)  
[x] service (+ controller?) layer endpointidele. mingi, status codeid ja errorid  
[x] esmased endpointid  
[x] backi db set up  
[x] esmased andmestruktuurid  
