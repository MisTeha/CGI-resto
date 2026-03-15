# CGI 2026 suvepraktika proovitöö
**Autor:** Uku Mihkel Kolka  
**Teema:** Restorani broneeringusüsteem

## Backend

Olen väga tuttav veebirakendustele backendi loomisega ja tavadega, kuid Spring Boot-i õppisin süvitsi esimest korda selle projekti tegemise käigus. Arendades kasutasin võimalikult palju Spring-i dokumentatsiooni ja näiteid, projekti failistruktuur on üksnes keelemudeli pakutud (mõnel juhul parandatud).

### Õppisin / tuletasin meelde / teadvustasin 

- Java Stream-id on väga (minuarust) väga kohmakad, aga väga rikkalikud ning palju kasutatud *(teadvustasin)*
- Javas (ja mujal) API-des on tihti kasutusel lisaks *handler*-ile ja *service*-ile **DTO**-d, mis map-ivad endpointi ja service vahel andmetüüpe. Neid ise aga kasutusele ei võtnud ja hõlmasin seda *service* tasemel. *(õppisin)*
- API-t luues arvestada sellega, mis frontendis toimuma hakkab, on keeruline. Pean silmas seda, et kas nt tagastada ainult laudade ID-sid või tervet objekti. (tuletasin meelde)

### Komistuskohad

- Spring-i "maagia", nii koodi kirjutades kui debugimisel tuli maagia sama palju kasuks kui kahjuks oma keerukusega.
- Raske oli otsustada, milliseid filtreid rakendada andmebaasis ning milliseid *service* tasemel. Lõpuks otsustasin olla kuigi andmebaasi filtrite põhine (vt AvailabilityService).

## TODO

[] svelte kliendipoolne koristus:
[] - svelte v5-pärane kood
[] - komponendid Daisy ja shadcn kasutusel, mitte custom gpt kood
[] suvaliste reserveeringute gen db loomisel  
[] hoida teste up to date, (pole prioriteet)  
[x] frontend'i loogika (serveri poole) refactor 
[x] alustada frontendiga  
[x] laudade scoring valem / midagi muud (aga võiks veel muutuda)  
[x] service (+ controller?) layer endpointidele. mingi, status codeid ja errorid  
[x] esmased endpointid  
[x] backi db set up  
[x] esmased andmestruktuurid  
