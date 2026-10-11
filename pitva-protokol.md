
# Cvičení 4.1 — Dekompilační protokol

## 1. Data class — Mesto

Zdrojová konstrukce:
`data class Mesto(val nazev: String, val teplota: Double)`

### Výsledek analýzy

Kompilátor automaticky vytvořil:

- Privátní final pole nazev a teplota.
- Konstruktor a metody getNazev(), getTeplota().
- Metody component1() a component2().
- Metody copy() a copy$default().
- Metody equals(), hashCode() a toString().
- Pole $stable přidané Compose compilerem.

### Pozorování

Metoda copy() vytváří novou instanci třídy Mesto.
Metoda copy$default() využívá bitovou masku pro
určení výchozích argumentů.

Metoda equals() porovnává jednotlivé vlastnosti
objektů, nikoliv pouze jejich reference.

Kotlin tedy generuje podstatnou část implementace
datové třídy automaticky.


## 2. Object — Konfigurace

Zdrojová konstrukce:
`object Konfigurace`

### Výsledek analýzy

Kompilátor vytvořil:

- Final třídu Konfigurace.
- Statické final pole INSTANCE.
- Privátní konstruktor Konfigurace().
- Statický inicializační blok.
- Statické pole ZAKLADNI_URL.
- Pole $stable přidané Compose compilerem.

### Pozorování

Instrukce NEW alokuje objekt Konfigurace.

Instrukce INVOKESPECIAL zavolá privátní
konstruktor objektu.

Instrukce PUTSTATIC uloží referenci
do statického pole INSTANCE.

Privátní konstruktor zabraňuje běžnému
vytváření dalších instancí.

Kotlin implementuje object pomocí
standardních mechanismů JVM.



## 3. Extension function — naStupneCelsia

Zdrojová konstrukce:
`fun Double.naStupneCelsia(): String`

### Výsledek analýzy

Kompilátor vytvořil:

- Třídu PitvaKt.
- Statickou metodu naStupneCelsia(double).
- Původní receiver Double převedl na parametr double.

### Pozorování

Extension funkce se nepřidává do třídy Double.
Kompilátor ji reprezentuje statickou metodou
ve třídě PitvaKt.


## 4. Higher-order function — vypisVse

Zdrojová konstrukce:
`fun vypisVse(mesta: List<Mesto>, akce: (Mesto) -> Unit)`

### Výsledek analýzy

Kompilátor vytvořil:

- Statickou metodu vypisVse().
- Parametr List<Mesto>.
- Parametr Function1<Mesto, Unit>.
- Volání metody Function1.invoke().

### Pozorování

Funkční typ (Mesto) -> Unit je na JVM
reprezentován rozhraním Function1.

Cyklus forEach byl převeden na iteraci
pomocí Iterator, hasNext() a next().


## 5. Suspend function — pockejAVrat

Zdrojová konstrukce:
`suspend fun pockejAVrat(): Int`

### Výsledek analýzy

Kompilátor vytvořil:

- Statickou metodu vracející Object.
- Parametr Continuation<Integer>.
- Pomocnou třídu PitvaKt$pockejAVrat$1.
- Stavovou proměnnou label.
- Instrukci tableswitch pro přepínání stavů.
- Kontrolu hodnoty COROUTINE_SUSPENDED.

### Pozorování

Funkce suspend byla transformována
na stavový automat.

Proměnná label rozlišuje stav před
pozastavením a po obnovení korutiny.

Funkce delay(100) může vrátit
COROUTINE_SUSPENDED.

Po pokračování funkce vrací hodnotu 42.

