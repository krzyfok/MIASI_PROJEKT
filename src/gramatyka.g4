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
    | defMacroCmd
    | callMacroCmd
    | ifExistsCmd
    | archiveCmd
    | extractCmd
    | healthCmd
    | backupCmd
    | cleanupCmd
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
defMacroCmd  : 'zdefiniuj' 'makro' name=ID ':' stat (',' stat)* ;
callMacroCmd : 'uruchom' 'makro' name=ID ;
ifExistsCmd : 'jeśli' 'istnieje' typ=('plik' | 'katalog')? cel=arg 'to' thenBlock+=stat (',' thenBlock+=stat)* ('w przeciwnym razie' elseBlock+=stat (',' elseBlock+=stat)*)? ;
archiveCmd : 'spakuj' cel=arg 'do' archiwum=arg ;
extractCmd : 'rozpakuj' archiwum=arg 'do' cel=arg ;
healthCmd  : 'pokaż' 'stan' 'systemu' ;
backupCmd  : 'zabezpiecz' cel=arg ;
cleanupCmd : 'posprzątaj' 'tutaj' ;

locationArgs
    : 'jestem' #pwd
    | 'jest plik' cel=arg #locate_file
    ;


arg : PATH
    | STRING
    | ID
    ;

NUMBER : [0-9]+ ;
STRING : '"' .*? '"' ;
ID   : [a-zA-Z_][a-zA-Z0-9_]* ;
PATH : [a-zA-Z0-9_./\-]+ ;


WS : [ \t\r\n]+ -> skip ;