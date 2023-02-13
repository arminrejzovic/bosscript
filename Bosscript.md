# Bosscript

<style>
.keyword{
    color: #cc7832;
}

.string{
    color: #6a8759;
}

.number{
    color: #6897BB;
}

.comment{
    color: #808080;
}

.prop{
    color: #9876AA;
}

.builtin{
    color: #ffc66d;
}

</style>

## Comments

Comments start with <code>//</code> and are single-line only. Examples:

<pre>
<span class="comment">// Funkcija koja pretvara slike u base64 format</span>
<span class="keyword">funkcija</span> base64(slika: tekst): tekst{
    ...
}
</pre>

## Entry point

Bosscript files (.boss) do not have an entry point. Code is interpreted in order, from top to bottom.

## Imports

You can import code from other Bosscript scripts using the <code>#paket</code> command. Examples:

<pre>
<span class="keyword">#paket</span> <span class="string">"primjer.boss"</span>
<span class="keyword">#paket</span> <span class="string">"matematika.boss"</span>
<span class="keyword">#paket</span> <span class="string">"../primjeri/zadaca1.boss"</span>
</pre>

Files that are in the working directory are imported by name only. Standard library implementations are also implemented
by name only. Files outside the working directory can be imported using relative paths. Import statements can be anywhere
in the script, but we recommend writing them at the top.

## Data types and variables

Bosscript has the following data types:

- `broj` representing all numbers, including integers and floating point numbers
- `tekst` representing strings
- `logicki` representing booleans
- `objekat` special type for objects
- `nedefinisano` special type encompassing `undefined` and `null`
- `svi` special type mostly meant for function parameters whose type hasn't been specified. Equivalent to `any` in TypeScript

Bosscript also has arrays, which are typed as follows: `type[]`.

### Variables

Variables in Bosscript can be declared with two keywords, namely `var` and `konst`. Variables declared with `var` are mutable,
while variables declared with `konst` are constant.

<pre>
<span class="keyword">var</span> x = <span class="string">"x"</span> 
<span class="keyword">konst</span> pi = <span class="number">3.14</span>
</pre>

Variables can have type annotations. If a variable is given an initial value, it is not necessary to explicitly declare 
its type. However, if no initial value is given, a type annotation must be provided.

<pre>
<span class="comment">// No type annotation is needed</span>
<span class="keyword">var</span> x = <span class="string">"x"</span>
<span class="keyword">konst</span> pi = <span class="number">3.14</span>

<span class="comment">// But we can specify it if we want</span>
<span class="keyword">var</span> y: tekst = <span class="string">"y"</span>
<span class="keyword">konst</span> e: broj = <span class="number">2.718</span>

<span class="comment">// If no initial value is provided, then the variable must be typed</span>
<span class="keyword">var</span> z 
<span class="comment">//  ^</span>
<span class="comment">//  |</span>
<span class="comment">//  Greska: Varijabla 'z' nije inicijalizirana ispravno.</span>

<span class="comment">// This is fine</span>
<span class="keyword">var</span> v: tekst
</pre>

Constants must have an initial value, regardless of type annotations.

<pre>
<span class="keyword">konst</span> pi: broj 
<span class="comment">//            ^</span>
<span class="comment">//            |</span>
<span class="comment">//            Greska: Konstante moraju imati definisanu vrijednost.</span>
</pre>

Arrays are declared in the same way:

<pre>
<span class="keyword">var</span> niz = [<span class="number">1</span>,<span class="number">2</span>,<span class="number">3</span>]
<span class="keyword">var</span> imena = [<span class="string">"Belid"</span>, <span class="string">"Safet"</span>, <span class="string">"Aldino"</span>]
</pre>

The type of `niz` will be `broj[]`, while the type of `imena` will be `tekst[]`.

Objects are declared in a different way. The `objekat` keyword is used. By default, objects are mutable, as if the `var`
keyword was used. To declare a constant object, use `konst objekat`

<pre>
<span class="keyword">objekat</span> korisnik = {
    <span class="prop">ime</span>: "Armin",
    <span class="prop">email</span>: "armin@mail.com"
}

<span class="keyword">konst</span> <span class="keyword">objekat</span> dimenzije = {
    <span class="prop">x</span>: 900,
    <span class="prop">y</span>: 600
}
</pre>
This is true only for simple objects. Objects, in the sense of class instances, are declared with `var` and `konst`, just
like other variables.

<pre>
<span class="keyword">klasa</span> Primjer{...}

<span class="keyword">var</span> primjer = Primjer()
</pre>

## Printing to the standard output

Bosscript comes with the <code>ispis()</code> function, which prints to the standard library. There are two more functions,
namely <code>upozorenje()</code> and <code>greska()</code>, akin to JavaScript's <code>console.warn()</code> and
<code>console.error()</code> functions. All three functions work the same - one argument is accepted.

<pre>
<span class="builtin">ispis</span>(<span class="string">"Pozdrav svima"</span>)

<span class="keyword">var</span> ime = <span class="string">"Armin"</span>
<span class="builtin">ispis</span>(<span class="string">"Pozdrav "</span> + ime)
<span class="builtin">ispis</span>(ime)

<span class="builtin">upozorenje</span>(<span class="string">"Niste dali dozvolu za lokaciju")</span>

<span class="builtin">greska</span>(<span class="string">"Stranica trougla ne može biti negativan broj!"</span>)
</pre>

## Functions

All functions in Bosscript are declared using the `funkcija` keyword. Function names can follow any convention, but we 
recommend lower camelcase. Parameters are passed inside parentheses and separated with commas. Each parameter must have 
a unique name. Type annotations are optional - if none is provided, `svi` is implicitly chosen. The function return type
must be specified if it is not void.

Returns are specified with the `vrati` keyword. A function can have any number of returns.

<pre>
<span class="keyword">funkcija</span> veci(a: broj, b: broj): broj{
    <span class="keyword">ako</span> (a > b){
        <span class="keyword">vrati</span> a
    }
    <span class="keyword">inace</span> {
        <span class="keyword">vrati</span> b
    }
}
    
<span class="comment">// Funkcija bez povratnog tipa</span>
<span class="keyword">funkcija</span> ispisiPozdrav(ime: tekst){
    <span class="builtin">ispis</span>(<span class="string">"Zdravo "</span> + ime)
}

<span class="comment">// Funkcija bez povratnog tipa sa parametrom bez tipa</span>
<span class="keyword">funkcija</span> uradiNesto(varijabla){
    ...
}
</pre>

## Flow control

Just like any other language, Bosscript has if-else statements. If blocks are defined using the keyword `ako`. Any number
of if blocks may be chained using `ili ako`, which is equivalent to `else if`. Else blocks are defined using the keyword
`inace`. You can have only one else block. Bosscript also has the `osim ako` block, which is standalone and works just 
like `unless` in Ruby. The language doesn't have switch statements.

```typescript
funkcija primjer(x:broj): tekst{
    ako (x % 2 == 0 %% x < 100){
        vrati "Parni broj manji od 100"
    }
    ili ako (x % 2 > 0 && x < 100){
        vrati "Parni broj veci od 100"
    }
    inace {
        vrati "Neparan broj"
    }
}

funkcija ispisiPozdrav(ime: tekst){
    // This block will run only if the length of ime is greater than 3
    osim ako(ime.duzina() < 3){
        ispis("Pozdrav ", ime)
    }
}
```

## Loops

Bosscript has two types of loops - for loops(`za svako`) and while loops (`dok`). 

### For loops

This is what a for loop looks like in Bosscript:

```typescript
za svako(var x od 0 do 10){
    ispis(x)
}
```
The example above shows how you would print 10 numbers in Bosscript. The for loop starts with the keywords `za svako`, followed by parentheses.
The loop variable is declared with `var`. The bounds of the loop are defined using the keywords `od` - which represents
the starting point of the loop, and `do` - which represents the ending point of the loop. 

Bosscript can infer the step based on the provided start and end points:

```typescript
za svako(var x od 10 do 0){
    ispis(x)
}
```
In this case, Bosscript sees that 10 is larger than 0 and knows that the step needs to be decrementing. The example above
prints numbers from 10 to 0. 

However, we can explicitly define the step if needed, using the `korak` keyword:

```typescript
za svako (var i od 0 do 10 korak 2){
    ispis(x)
}
```

The example above increments the loop variable by 2 each round. The outcome is that the numbers [0,2,4,6,8] are printed.
We can also provide a negative number for the step. Floating point numbers are also fine:

```typescript
za svako (var i od 10 do 0 korak -0.5){
    ispis(x)
}
```

There is usually no need to do this, but it is supported.

Any of the loop parameters may be a computed value. For example, we can use the length of an array or string as one of 
the loop bounds. Even the step can be calculated:

```typescript
var niz = [2,5,1,52,12,65]
za svako (var i od 0 do niz.duzina() korak izracunajKorak()){
    ispis(i)
}
```

Do note that the values must be numbers (i.e. the `broj` type). Be careful with step, as to not write an infinite loop.

### While loops

While loops are defined using the keyword `dok`. This is what a while loop looks like in Bosscript:

```typescript
var i = 0

dok(i < 10){
    ispis(i)
    i++
}
```

while loops work exactly the same as in most C-family languages. An expression that evaluates to a boolean is provided
between the parentheses and the loop runs as long as that expression evaluates to `true`.

To get an infinite loop, one could write any of the following:

```typescript
dok(tacno){
    ...
}

// any non zero number evaluates to true
dok(1){
    ...
}

dok(1 == 1){
    ...
}
```

## Classes

All classes are defined using the `klasa` keyword. Each class must have a constructor method named `konstruktor`. There 
can only be one constructor method in a class. It can take any number of parameters, including no parameters at all. 

<pre>
<span class="keyword">ako</span>(x > 10 <span class="keyword">ili</span> x < 5 <span class="keyword">te</span> x%2==0){
    
}
</pre>

