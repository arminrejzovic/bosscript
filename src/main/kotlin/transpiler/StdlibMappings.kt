package transpiler

import interpreter.values.Broj

val stdlibMappings = hashMapOf<String, String>(
    // Broj
    "zaokruzi" to "+toFixed",
    "tekst" to "toString",
    // Niz
    "duzina" to "length",
    "dodaj" to "push",
    "dodajNaPocetak" to "unshift",
    "spoji" to "concat",
    "poravnaj" to "flat",
    "izbaci" to "pop",
    "izbaciPrvi" to "shift",
    "sortiraj" to "sort",
    "sortirajSa" to "sort",
    "isijeci" to "slice",
    "primijeni" to "map",
    "zaSvaki" to "forEach",
    // Tekst
    "zavrsavaNa" to "endsWith",
    "podtekst" to "substring",
    "podtekstIndeks" to "indexOf",
    "zamijeni" to "replace",
    "sadrzi" to "includes",
    "malimSlovima" to "toLocaleLowerCase",
    "velikimSlovima" to "toLocaleUpperCase",
    "srezi" to "trim",
    "razdvoji" to "split",
    "pocinjeNa" to "startsWith",
    // JSON
    "objekatIzJSON" to "JSON.parse",
    "JSONTekst" to "JSON.stringify",
    // Math
    "apsolutnaVrijednost" to "Math.abs",
    "pi" to "Math.PI",
    "arccos" to "Math.acos",
    "arcsin" to "Math.asin",
    "sin" to "Math.sin",
    "cos" to "Math.cos",
    "tg" to "Math.tan",
    "arctg" to "Math.atan",
    "korijen" to "Math.sqrt",
    "e" to "Math.E",
    "eNa" to "Math.exp",
    "ln" to "Math.log",
    "log10" to "Math.log10",
    "log2" to "Math.log2",
    "max" to "Math.max",
    "min" to "Math.min",
    "zaokruzi" to "Math.round",
    "kubniKorijen" to "Math.cbrt",
    "beskonacno" to "Infinity",
    "minusBeskonacno" to "-Infinity",
    // Globals
    "ispis" to "console.log",
    "upozorenje" to "console.warn",
    "greska" to "console.error",
    "unos" to "prompt",
    "postoji" to "(i) => {return i != null}",
    "brojOd" to "parseFloat",
    "logickiOd" to "(b) => {return (b === 'tacno' || b ==='tačno' || b === 'true')}",
    "nizOd" to "new Array"
)