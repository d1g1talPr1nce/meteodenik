# Cvičení 4.1 — Dekompilační protokol

## 1. Data class — Mesto

Ve zdrojovém kódu jsem napsal:

`data class Mesto(val nazev: String, val teplota: Double)`

Kompilátor vytvořil soukromá pole `nazev` a `teplota`, konstruktor a gettery. Doplnil také metody `component1()`, `component2()`, `copy()`, `copy$default()`, `equals()`, `hashCode()` a `toString()`. Ve výstupu bylo i pole `$stable`, které přidal Compose compiler.

Metoda `copy()` vytvoří nový objekt s možností změnit některé hodnoty. Pomocná metoda `copy$default()` pomocí bitové masky určuje, které hodnoty se převezmou z původního objektu. Metoda `equals()` porovnává název a teplotu, takže dva různé objekty se stejnými hodnotami mohou být shodné.

## 2. Object — Konfigurace

Ve zdrojovém kódu jsem použil:

`object Konfigurace`

Ve výsledku vznikla třída `Konfigurace` se soukromým konstruktorem a statickým polem `INSTANCE`, které obsahuje její jedinou běžně používanou instanci. Dále obsahuje pole `ZAKLADNI_URL` a pole `$stable` přidané Compose compilerem.

Ve statickém inicializačním bloku se objekt vytvoří instrukcí `NEW`, pomocí `INVOKESPECIAL` se zavolá konstruktor a `PUTSTATIC` uloží odkaz do `INSTANCE`. Díky soukromému konstruktoru nelze běžným způsobem vytvářet další instance.

## 3. Extension funkce — naStupneCelsia

Ve zdrojovém kódu jsem napsal:

`fun Double.naStupneCelsia(): String`

Funkce se přeložila na statickou metodu `naStupneCelsia(double)` ve třídě `PitvaKt`. Hodnota, nad kterou funkci volám, se předává jako parametr typu `double`.

Do samotné třídy `Double` se tedy žádná nová metoda nepřidává. Kotlin pouze umožňuje pohodlnější zápis jejího volání.

## 4. Funkce s funkčním parametrem — vypisVse

Ve zdrojovém kódu jsem napsal:

`fun vypisVse(mesta: List<Mesto>, akce: (Mesto) -> Unit)`

Vznikla statická metoda, která přijímá seznam měst a parametr typu `Function1`. Ten představuje předanou funkci, která dostane jedno město a nevrací žádný užitečný výsledek.

Předaná funkce se volá přes metodu `invoke()`. Zápis `forEach` se ve výstupu změnil na procházení seznamu pomocí iterátoru a metod `hasNext()` a `next()`.

## 5. Suspend funkce — pockejAVrat

Ve zdrojovém kódu jsem napsal:

`suspend fun pockejAVrat(): Int`

Po překladu metoda vrací `Object` a má navíc parametr `Continuation`. Vznikla také pomocná třída `PitvaKt$pockejAVrat$1`, která uchovává stav potřebný pro pokračování funkce.

Proměnná `label` a instrukce `tableswitch` určují, odkud má funkce pokračovat. Pokud se při `delay(100)` pozastaví, vrátí značku `COROUTINE_SUSPENDED`. Po obnovení dokončí zbytek kódu a vrátí hodnotu `42`. Návratový typ `Object` umožňuje vrátit jak tuto značku, tak zabalený číselný výsledek.
