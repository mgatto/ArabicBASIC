lexer grammar ArabicBASICLexer;

@header {
package com.lisantra.arabicbasic;
}

COMMENT: '//' ~[\r\n]* EOL -> channel(HIDDEN);
STRING: '"' (~'"' | '\\"')* '"';
LET_KW: 'صار';
ASSIGN: '=';
LBRACK: '[';
RBRACK: ']';
LPAREN: '(';
RPAREN: ')';
SEMICOLON: ';';
ARABIC_SEMICOLON: '\u061B';
LE: '<=';
GE: '>=';
NE: '<>';
LT: '<';
GT: '>';
POW_OP: '^';
MUL_OP: '*';
DIV_OP: '/';
PLUS_OP: '+';
MINUS_OP: '-';
MOD_OP: 'MOD';
ARRAY_KW: 'مصفوفة';
ARRAY_KW_ALT: 'مصفوفه';
IF_KW: 'اذا';
IF_KW_ALT: 'إذا';
THEN_KW: 'ثم';
ELSE_IF_KW: 'وإلا اذا';
ELSE_IF_KW_ALT: 'وإلا إذا';
ELSE_KW: 'وإلا';
END_IF_KW: 'ختام اذا';
FOR_KW: 'لكل';
TO_KW: 'حتى';
STEP_KW: 'درجة';
WHILE_KW: 'طالما';
END_WHILE_KW: 'ختام طالما';
DEF_KW: 'دالّة';
DEF_KW_ALT: 'دالة';
RUN_KW: 'اجري';
RUN_KW_ALT: 'إجري';
PRINT_KW: 'اطبع';
PRINT_KW_ALT: 'إطبع';
INPUT_KW: 'ادخل';
INPUT_KW_ALT: 'أدخل';
STACK_FACTORY_KW: 'مكدس';
STACK_PUSH_KW: 'ادفع';
STACK_PUSH_KW_ALT: 'إدفع';
STACK_POP_KW: 'اسحب';
STACK_POP_KW_ALT: 'إسحب';
STACK_PEEK_KW: 'انظر';
STACK_PEEK_KW_ALT: 'أنظر';
NOT_KW: 'ليس';
AND_KW: 'ايضاً';
OR_KW: 'ام';
OR_KW_ALT: 'او';
OR_KW_ALT2: 'أو';
BOOL_TRUE: 'صحيح';
BOOL_FALSE: 'خطأ';
ABS_FN: 'ABS';
COS_FN: 'COS';
SIN_FN: 'SIN';
TAN_FN: 'TAN';
LOG_FN: 'LOG';
EXP_FN: 'EXP';
INT_FN: 'INT';
SQR_FN: 'SQR';
RND_FN: 'RND';
LEFT_FN: 'LEFT';
RIGHT_FN: 'RIGHT';
MID_FN: 'MID';
LEN_FN: 'LEN';
CHR_FN: 'CHR';
ORD_FN: 'ORD';
// Shared morphology fragments for adjectival/predicate keyword variants.
fragment FEM_SUFFIX: 'ة';
fragment QMARK_AR: '؟';
// Keep keyword-token introduction minimal to avoid broad lexer-priority shifts vs IDENTIFIER.
NEXT_ADJ: 'التالي' FEM_SUFFIX?;
STACK_EMPTY_PRED: 'فارغ' FEM_SUFFIX? QMARK_AR;
// Arabic-script letters used across Arabic/Persian/Urdu (letters only; punctuation excluded).
fragment ARABIC_LETTER:
	[\u0621-\u063A\u0641-\u064A\u066E-\u066F\u0671-\u06D3\u06D5\u06EE-\u06EF\u06FA-\u06FC\u06FF];
// Arabic combining marks/diacritics.
fragment ARABIC_MARK:
	[\u064B-\u065F\u0670\u06D6-\u06ED\u08D3-\u08E1\u08E3-\u08FF];
// Join controls (not whitespace): ZWNJ, ZWJ.
fragment JOIN_CONTROL: [\u200C\u200D];
// Western + Arabic-Indic + Eastern Arabic-Indic digits.
fragment ID_DIGIT: [0-9\u0660-\u0669\u06F0-\u06F9];
// Start with an Arabic-script letter, then allow letters/marks/digits/_/tatweel which is \u0640
IDENTIFIER:
	ARABIC_LETTER (
		ARABIC_LETTER
		| ARABIC_MARK
		| JOIN_CONTROL
		| ID_DIGIT
		| '_'
		| '\u0640'
	)*;
COMMA: [,\u060C];
// Arabic and ASCII commas are both accepted for argument/identifier lists.
REAL: DIGIT [.,\u060C\u066B] DIGIT+;
//Western Arabic numbers are increasingly in use throughout the Arabic-speaking world.
INTEGER: [0\u0660] | [1-9\u0661-\u0669] DIGIT*;
EOL:
	// Extended Unicode line separators improve copy/paste robustness.
	'\r\n'
	| '\n'
	| '\r'
	| '\u0085' // NEL
	| '\u2028' // Line Separator
	| '\u2029'; // Paragraph Separator
WS:
	// Unicode-aware whitespace skipping (excluding line terminators handled by EOL).
	[\u0009\u000B\u000C\u0020\u00A0\u1680\u2000-\u200A\u202F\u205F\u3000]+ -> skip;
// Optional: tolerate BOM if present in source files.
BOM: '\uFEFF' -> skip;
// fragments are not token themselves, but non-atomic components of tokens
fragment DIGIT: [0-9\u0660-\u0669];
//TODO: wierdly, this will allow intermixing, when I really want Western OR Eastern Arabic numbers