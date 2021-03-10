grammar Bool;

expression: OPEN expression CLOSE
          | NOT expression
          | left=expression op=binary right=expression
          | NOT OPEN left=expression op=binary right=expression CLOSE
          | bool
          | IDENTIFIER;

binary: AND | OR | IMP | AQ;

bool: TRUE | FALSE;

AND: 'AND';
OR: 'OR';
NOT: 'NOT';
IMP: 'IMP';
AQ: 'AQ';

TRUE: 'TRUE' | '1';
FALSE: 'FALSE' | '0';

OPEN: '(';
CLOSE: ')';

IDENTIFIER: [a-zA-Z];

WS: [ \r\t\u000C\n]+ -> skip;