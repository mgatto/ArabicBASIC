parser grammar ArabicBASIC;

options {
	tokenVocab = ArabicBASICLexer;
}

@header {
package com.lisantra.arabicbasic;
}

program: block EOF;
block: statement*;
statement:
	COMMENT // shouldn't have EOL because it's a terminal
	| blank // doesn't need EOL
	| simpleAssignment EOL
	| arrayAssignment EOL
	| singleLineConditional //doesn't need EOL because it ends with a statement = already has EOL
	| conditionalBlock EOL
	| forLoop EOL
	| whileLoop EOL
	| defineSingleLineFunction EOL
	| callFunction EOL
	| print EOL
	| input EOL;
simpleAssignment:
	'صار' name += IDENTIFIER (COMMA name += IDENTIFIER)* '=' expression;
// Sequence with Terminator pattern
arrayAssignment: IDENTIFIER '[' subscript ']' '=' expression;
// accept taa marbuta/haa drift in array factory keyword.
arrayCreation: ('مصفوفة' | 'مصفوفه') '(' arraySize ')';
conditionalBlock:
	// tolerate hamza spelling variants in IF / ELSE IF forms.
	('اذا' | 'إذا') tests += booleanExpression 'ثم' EOL block (
		('وإلا اذا' | 'وإلا إذا') tests += booleanExpression 'ثم' EOL block
	)* ('وإلا' EOL block)? 'ختام اذا';
// tolerate hamza spelling variants in single-line IF.
singleLineConditional: ('اذا' | 'إذا') booleanExpression 'ثم' statement;
//Allow expressions for upper bound at least?
forLoop:
	'لكل' control = IDENTIFIER '=' lower = INTEGER 'حتى' upper = expression (
		'درجة' '=' step = INTEGER
	)? EOL block next = IDENTIFIER NEXT_ADJ;
whileLoop:
	'طالما' test = booleanExpression EOL block 'ختام طالما';
// حدِّد might be better! used in qalb
defineSingleLineFunction:
	// V1 alias: accept both دالّة and دالة spellings.
	('دالّة' | 'دالة') funcName = IDENTIFIER '(' arg = variable ')' '=' expression;
//DEF FN cube(a) = a^3
callFunction:
	// V1 alias: imperative verb accepts hamza/no-hamza forms.
	('اجري' | 'إجري') funcName = IDENTIFIER '(' variable ')';
//this looks too much like arrayAccess!
print:
	// V1 alias: imperative verb accepts hamza/no-hamza forms.
	('اطبع' | 'إطبع') expression (
		spacer += (COMMA | ';' | '\u061B') expression
	)*;
input:
	// V1 alias: imperative verb accepts hamza/no-hamza forms.
	('ادخل' | 'أدخل') (
		prompt = STRING (spacer = (COMMA | ';' | '\u061B'))
	)? var += IDENTIFIER (COMMA var += IDENTIFIER)*;
blank: WS* EOL;
expression: // List the rules from highest -> lowest precedence
	// Put built-in function matches here BEFORE identifier to take advantage of first-match
	name = (
		'ABS'
		| 'COS'
		| 'SIN'
		| 'TAN'
		| 'LOG'
		| 'EXP'
		| 'INT'
		| 'SQR'
		| 'RND'
	) '(' expression ')' # mathFunction
	| name = ('LEFT' | 'RIGHT' | 'MID' | 'LEN' | 'CHR' | 'ORD') '(' arg += variable (
		COMMA arg += variable
	)? ')'				# stringFunction
	| 'مكدس' '(' ')'	# stackFactory
	// stack imperative verbs accept hamza/no-hamza forms.
	| ('ادفع' | 'إدفع') '(' stack = variable (
		COMMA value = expression
	) ')'											# stackPushFunction
	| ('اسحب' | 'إسحب') '(' stack = variable ')'	# stackPopFunction
	| ('انظر' | 'أنظر') '(' stack = variable ')'	# stackPeekFunction
	// predicate adjective uses dedicated lexer token (strict Arabic question mark only).
	| STACK_EMPTY_PRED '(' stack = variable ')'	# stackEmptyFunction
	| IDENTIFIER '[' subscript ']'					# arrayAccess
	| '-' expression								# unary
	| <assoc = right>expression '^' expression		# exponentation
	| expression op = 'MOD' expression				# modulus
	| expression op = ('*' | '/') expression		# mulDiv
	| expression op = ('+' | '-') expression		# addSub
	| arrayCreation									# arrayFactory
	| callFunction									# functionCall
	//TODO get rid of  "Variable" = too many layers of abstraction
	| variable				# term
	| '(' expression ')'	# nested;
subscript: INTEGER | IDENTIFIER;
// expression's left recursion will break if I put "expression" here...
arraySize: expression;
//can expand it from Integer --> Expression(Numerical), maybe catch string "size" in the parser.
booleanExpression: // does not support "expression" in the terms because of left recursion?
	booleanExpression comp = (
		'>'
		| '<'
		| '<='
		| '>='
		| '='
		| '<>'
	) booleanExpression # comparitiveBoolean
	//جزم
	| 'ليس' booleanExpression # negatingBoolean
	//  و   ضرب
	| booleanExpression 'ايضاً' booleanExpression # logicalAnd
	//او جمع allow OR spelling variants commonly typed by Arabic users.
	| booleanExpression ('ام' | 'او' | 'أو') booleanExpression	# logicalOr
	| expression												# expressionBoolean
	| variable													# atomicBoolean //variable: "Like in C, any non-zero value is interpreted as True"
	| '(' booleanExpression ')'									# nestedBoolean;
variable:
	IDENTIFIER			# name
	| (INTEGER | REAL)	# numeric
	| STRING			# text
	| ('صحيح' | 'خطأ')	# bool;