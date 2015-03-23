# Verslag

## Activities

### MainActivity

Dit is het hoofdscherm, waarin het spel wordt gespeeld. Hier is te zien wie er aan de beurt is en wat het woord tot nu toe is. 

<img src='Screenshots/MainActivity/MainActivity0.png' width = '216', height = '384'/>
<img src='Screenshots/MainActivity/MainActivity1.png' width = '216', height = '384'/>

In dit scherm zijn er ook een aantal menu-opties toegevoegd, namelijk "Restart game", "Change players" en "Change language".

<img src='Screenshots/MainActivity/MainActivity2.png' width = '216', height = '384'/>

In de EditText kan een letter geplaatst worden en om een zet te doen, moet er op Enter gedrukt worden via het on-screen-toetsenbord. Als er daarna niet wordt gewonnen, is de andere speler aan de beurt en wordt het woord direct bijgewerkt.

<img src='Screenshots/MainActivity/MainActivity3.png' width = '216', height = '384'/>

##### protected void onCreate(Bundle savedInstanceState)
Initialiseren van de widgets en de lijst van dictionaries. Het roept loadDictionaries en initiateGameState aan.

##### public boolean onCreateOptionsMenu(Menu menu)
Creeeren van het menu.

##### public boolean onOptionsItemSelected(MenuItem item) 
Het spel kan worden herstart, er kan een andere taal worden gekozen en er kunnen andere namen voor de spelers worden gekozen.

##### public void loadDictionaries()
Laden van de dictionaries voor Engels en Nederlands. Beide dictionaries worden in de lijst all_dictionaries geplaatst.

##### public void changePlayerNames()
Een intent wordt gemaakt om naar NameActivity te gaan met startActivityForResult, met de namen van de spelers en de highscores-gegevens. 

##### protected void onActivityResult(int requestCode, int resultCode, Intent data)
Afhandelen van de resultaten van intents die van NameActivity, WinActivity of LanguageActivity kwamen.

##### protected void onStart()
Als er nog geen namen zijn, dan wordt changeNames() aangeroepen. Anders ofwel display() of toWinActivity().

##### public void enterLetter()
Dit wordt aangeroepen als er op er op de Enter knop in het toetsenbord is gedrukt. Er wordt een stap gemaakt in de gamePlay met de ingevoerde letter.

##### public void display()
Toont de huidige spelsituatie.

##### public void toWinActivity()
Dit wordt aangeroepen als het spel afgelopen is. Er wordt een intent gemaakt om naar WinActivity te gaan met startActivityForResult, omdat het nodig is om te weten of er nog wordt gewisseld van spelers of niet.

##### protected void onStop()
Voordat de app wordt gestopt, wordt eerst saveGameState() aangeroepen om gegevens op te slaan.

##### public void onBackPressed()
Als er op 'terug' wordt gedrukt, gaat de app naar NameActivity, in plaats van dat de app afsluit. 

##### public void saveGameState()
Gegevens worden opgeslagen zoals de namen van de spelers, de taal en de gegevens van gamePlay en highScores die worden opgeslagen met gamePlay.saveGameState en highScores.saveGameState

##### public void initiateGameState()
Gegevens worden gevonden zoals de namen van de spelers, de taal en de gegevens van gamePlay en highScores die worden gevonden met gamePlay.recallGameState en highScores.recallGameState. 

### WinActivity

Als het spel is gewonnen, wordt er dit scherm getoond. Hier is de winnaar te zien, het woord en de reden waarom er gewonnen is. Onderaan staat de plaats in de High Scores lijst en eventueel de oude plaats als de speler in de ranking gestegen is. 

Er zijn verschillende knoppen in dit scherm. De knop met "New Game" laat een nieuw spel starten met dezelfde spelers en gaat dus naar MainActivity. De knop eronder laat ook een nieuw spel starten, maar komt eerst in NameActivity. Ten slotte is onderaan de knop om naar RankingActivity te gaan om de ranking te zien.

<img src='Screenshots/WinActivity/WinActivity0.png' width = '216', height = '384'/>
<img src='Screenshots/WinActivity/WinActivity1.png' width = '216', height = '384'/>

##### protected void onCreate(Bundle savedInstanceState)
Initialiseren met onder andere Boolean 'newNames' als false. Naar MainActivity wordt deze Boolean meegegeven om aan de gegeven of in MainActivity de functie changeNames() moet worden aangeroepen.

##### public void display()
Tonen van de winnaar et cetera.

##### public void backToMain()
Intent om terug te gaan naar MainActivity met newNames = false;

##### public void newGameButtonClicked(View view)
Naar backToMain.

##### public void toRankingActivity(View view)
Intent om naar RankingActivity te gaan.

##### public void changePlayerNames(View view)
Intent om terug te gaan naar MainActivity met newNames = true;

##### public void onBackPressed()
Naar backToMain.

### RankingActivity

Dit scherm toont de ranking van de 15 hoogst scorende spelers. Om terug te gaan naar WinActivity moet er op de terugknop gedrukt worden.

<img src='Screenshots/RankingActivity/RankingActivity0.png' width = '216', height = '384'/>

##### protected void onCreate(Bundle savedInstanceState)
Initialiseren van de widgets en aanroepen van display().

##### public void display()
Tonen van de gegevens door gebruik te maken van de functie highScores.makeString() die de highscores gegevens in een string zet.

### NameActivity

Hier kunnen de namen van de spelers worden gekozen. In de EditText bovenaan kunnen de namen worden getypt en beide EditTexts hebben een "clear"-knop eronder. 

<img src='Screenshots/NameActivity/NameActivity1.png' width = '216', height = '384'/>

Er kan ook gebruik worden gemaakt van de Spinners die alle namen bevatten van spelers die eerder een spel hebben gespeeld (en afgemaakt). Als er nog niet eerder namen gekozen zijn, dan worden de Spinners niet getoond.

<img src='Screenshots/NameActivity/NameActivity3.png' width = '216', height = '384'/>
<img src='Screenshots/NameActivity/NameActivity0.png' width = '216', height = '384'/>

Om het spel met de huidige namen te spelen kan er op de "Start"-knop worden gedrukt of op de Enter-toets in het on-screen-toetsenbord. 

<img src='Screenshots/NameActivity/NameActivity2.png' width = '216', height = '384'/>

##### protected void onCreate(Bundle savedInstanceState)
Initialiseer en haal informatie uit de intent over de huidige spelers en de high scores lijst.

##### public void setupEditText(EditText editText, String player_name)
Maakt een setOnKeyListener op de editText die startGame() aanroept als er op Enter wordt gedrukt.

##### public void setupSpinner(final Spinner spinner, final EditText nameEditText)
Maakt een adapter voor de Spinners op basis van de highScores gegevens.

##### public void startGame()
Maakt een intent om naar MainActivity te gaan met player1name en player2name.

##### public Boolean isValidName(String name)
Check of de keuze voor de namen valide is.

##### public String toNameStyle(String name)
Bewerken van een string zodat de eerste letter van een woord een hoofdletter is en de rest kleine letters.

##### public void clearEditText(View view)
Leegmaken van een EditText

##### public void startButtonClicked(View view)
Wordt aangeroepen als er op de Start-knop wordt gedrukt en gaat naar startGame().

### LanguageActivity

Tijdens een spel kan een speler ervoor kiezen om de taal te veranderen. Dan komt de speler in dit scherm, waarbij er gekozen kan worden tussen talen door middel van RadioButtons in een RadioGroup. De mogelijke talen zijn Nederlands en Engels. Als een van de talen is aangevinkt, gaat de app terug naar MainActivity.

<img src='Screenshots/LanguageActivity/LanguageActivity0.png' width = '216', height = '384'/>

##### protected void onCreate(Bundle savedInstanceState)
Initialiseren van de widgets en aanroepen van initRadio voor Engels en Nederlands. De huidige taal wordt gevonden door de string extra in de intent.

##### public void initRadio(final RadioButton radio, final String language)
Zet een setOnClickListener op de RadioButton. Als de RadioButton wordt aangevinkt, dan wordt de bijbehorende taal onthouden en sendBackResult() aangeroepen.

##### public void sendBackResult()
Maken van een intent met de nieuw gekozen taal en ga terug naar MainActivity.

## Classes

### Dictionary



### GamePlay

### HighScores


