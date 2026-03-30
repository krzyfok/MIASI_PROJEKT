grammar gramatyka;

program : stat* EOF ;

stat: copyCmd
    | deleteCmd
    | moveCmd
    | createCmd
    | listCmd
    | printCmd
    | loopCmd
    | cdirCmd
    | ldirCmd
    | locationCmd
    | numerateCmd
    ;


copyCmd   : 'skopiuj' typ=('plik' | 'katalog')? zrodlo=arg 'do' cel=arg ;
deleteCmd : 'usuń' typ=('plik' | 'katalog')? cel=arg ;
moveCmd   : 'przenieś' typ=('plik' | 'katalog')? zrodlo=arg 'do' cel=arg ;
createCmd : 'utwórz' typ=('plik' | 'katalog') cel=arg ;
listCmd   : 'pokaż' 'pliki' ('w' cel=arg)? ;
printCmd  : 'wypisz' tresc=arg ;
loopCmd   : 'powtórz' ile=NUMBER 'razy' ':' stat (',' stat)* ;
cdirCmd   : 'otwórz' typ=('plik' | 'katalog') cel=arg ;
ldirCmd   : 'opóść' 'katalog' cel=arg;
locationCmd : 'gdzie' locationArgs;
numerateCmd : 'ponumeruj' cel=arg;

locationArgs
    : 'jestem' #pwd
    | 'jest plik' cel=arg #locate_file
    ;


arg : PATH
    | STRING
    ;

NUMBER : [0-9]+ ;
STRING : '"' .*? '"' ;
PATH : [a-zA-Z0-9_./\-]+ ;


WS : [ \t\r\n]+ -> skip ;