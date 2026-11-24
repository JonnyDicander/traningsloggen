## Byggstruktur i appen

1. **Skapade en mall för en övning (`WorkoutItem.kt`)** – Varje övning fick ett unikt id, namn, kategori och en markering för om den är klar.

2. **Byggde logiken och minnet (`WorkoutViewModel.kt`)** – Här sparas listan med övningar och kod som gör att man kan lägga till, ändra, bocka av och ta bort övningar.

3. **Byggde själva skärmen (`MainActivity.kt`)** – Gjorde knappar, textfält, en räknare för klara övningar och en lista som visas på skärmen.

4. **La till filter på kategorier** – Gjorde så att man kan klicka på t.ex. *Bröst* eller *Alla* för att bara se övningar i just den kategorin.

5. **La till ångraknapp vid borttagning** – Gjorde så att man kan ångra om man råkar ta bort en övning.

6. **La till emojis** – Lade till emojis för tydligare kategorier och status.

7. **Justerade filterraden** – Gjorde så att alla kategorier syns med emojis utan att kapas.

## Felsökning
När jag byggde appen fick jag några felmeddelanden i koden, bland annat när jag la till nya funktioner och filter. Jag löste det genom att kolla felmeddelandena, rätta saknade funktioner och ändra hur värdet hämtades så att koden gick att bygga igen.

## Hjälpmedel
Jag har använt Android Studio och AI som stöd när jag byggde appen och felsökte problem. Det hjälpte mig att hitta var felet satt och komma vidare när jag fastnade.