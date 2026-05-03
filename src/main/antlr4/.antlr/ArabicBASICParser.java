// Generated from /Users/michaelgatto/Development/ArabicBASIC/src/main/antlr4/ArabicBASIC.g4 by ANTLR 4.13.1

package com.lisantra.arabicbasic;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class ArabicBASICParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59, 
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, T__65=66, 
		T__66=67, T__67=68, T__68=69, T__69=70, COMMENT=71, STRING=72, NEXT_ADJ=73, 
		STACK_EMPTY_PRED=74, IDENTIFIER=75, COMMA=76, REAL=77, INTEGER=78, EOL=79, 
		WS=80, BOM=81;
	public static final int
		RULE_program = 0, RULE_block = 1, RULE_statement = 2, RULE_simpleAssignment = 3, 
		RULE_arrayAssignment = 4, RULE_arrayCreation = 5, RULE_conditionalBlock = 6, 
		RULE_singleLineConditional = 7, RULE_forLoop = 8, RULE_whileLoop = 9, 
		RULE_defineSingleLineFunction = 10, RULE_callFunction = 11, RULE_print = 12, 
		RULE_input = 13, RULE_blank = 14, RULE_expression = 15, RULE_subscript = 16, 
		RULE_arraySize = 17, RULE_booleanExpression = 18, RULE_variable = 19;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "block", "statement", "simpleAssignment", "arrayAssignment", 
			"arrayCreation", "conditionalBlock", "singleLineConditional", "forLoop", 
			"whileLoop", "defineSingleLineFunction", "callFunction", "print", "input", 
			"blank", "expression", "subscript", "arraySize", "booleanExpression", 
			"variable"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\\u0635\\u0627\\u0631'", "'='", "'['", "']'", "'\\u0645\\u0635\\u0641\\u0648\\u0641\\u0629'", 
			"'\\u0645\\u0635\\u0641\\u0648\\u0641\\u0647'", "'('", "')'", "'\\u0627\\u0630\\u0627'", 
			"'\\u0625\\u0630\\u0627'", "'\\u062B\\u0645'", "'\\u0648\\u0625\\u0644\\u0627 \\u0627\\u0630\\u0627'", 
			"'\\u0648\\u0625\\u0644\\u0627 \\u0625\\u0630\\u0627'", "'\\u0648\\u0625\\u0644\\u0627'", 
			"'\\u062E\\u062A\\u0627\\u0645 \\u0627\\u0630\\u0627'", "'\\u0644\\u0643\\u0644'", 
			"'\\u062D\\u062A\\u0649'", "'\\u062F\\u0631\\u062C\\u0629'", "'\\u0637\\u0627\\u0644\\u0645\\u0627'", 
			"'\\u062E\\u062A\\u0627\\u0645 \\u0637\\u0627\\u0644\\u0645\\u0627'", 
			"'\\u062F\\u0627\\u0644\\u0651\\u0629'", "'\\u062F\\u0627\\u0644\\u0629'", 
			"'\\u0627\\u062C\\u0631\\u064A'", "'\\u0625\\u062C\\u0631\\u064A'", "'\\u0627\\u0637\\u0628\\u0639'", 
			"'\\u0625\\u0637\\u0628\\u0639'", "';'", "'\\u061B'", "'\\u0627\\u062F\\u062E\\u0644'", 
			"'\\u0623\\u062F\\u062E\\u0644'", "'ABS'", "'COS'", "'SIN'", "'TAN'", 
			"'LOG'", "'EXP'", "'INT'", "'SQR'", "'RND'", "'LEFT'", "'RIGHT'", "'MID'", 
			"'LEN'", "'CHR'", "'ORD'", "'\\u0645\\u0643\\u062F\\u0633'", "'\\u0627\\u062F\\u0641\\u0639'", 
			"'\\u0625\\u062F\\u0641\\u0639'", "'\\u0627\\u0633\\u062D\\u0628'", "'\\u0625\\u0633\\u062D\\u0628'", 
			"'\\u0627\\u0646\\u0638\\u0631'", "'\\u0623\\u0646\\u0638\\u0631'", "'-'", 
			"'^'", "'MOD'", "'*'", "'/'", "'+'", "'>'", "'<'", "'<='", "'>='", "'<>'", 
			"'\\u0644\\u064A\\u0633'", "'\\u0627\\u064A\\u0636\\u0627\\u064B'", "'\\u0627\\u0645'", 
			"'\\u0627\\u0648'", "'\\u0623\\u0648'", "'\\u0635\\u062D\\u064A\\u062D'", 
			"'\\u062E\\u0637\\u0623'", null, null, null, null, null, null, null, 
			null, null, null, "'\\uFEFF'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "COMMENT", 
			"STRING", "NEXT_ADJ", "STACK_EMPTY_PRED", "IDENTIFIER", "COMMA", "REAL", 
			"INTEGER", "EOL", "WS", "BOM"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "ArabicBASIC.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ArabicBASICParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ArabicBASICParser.EOF, 0); }
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			block();
			setState(41);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_block);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(43);
					statement();
					}
					} 
				}
				setState(48);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public TerminalNode COMMENT() { return getToken(ArabicBASICParser.COMMENT, 0); }
		public BlankContext blank() {
			return getRuleContext(BlankContext.class,0);
		}
		public SimpleAssignmentContext simpleAssignment() {
			return getRuleContext(SimpleAssignmentContext.class,0);
		}
		public TerminalNode EOL() { return getToken(ArabicBASICParser.EOL, 0); }
		public ArrayAssignmentContext arrayAssignment() {
			return getRuleContext(ArrayAssignmentContext.class,0);
		}
		public SingleLineConditionalContext singleLineConditional() {
			return getRuleContext(SingleLineConditionalContext.class,0);
		}
		public ConditionalBlockContext conditionalBlock() {
			return getRuleContext(ConditionalBlockContext.class,0);
		}
		public ForLoopContext forLoop() {
			return getRuleContext(ForLoopContext.class,0);
		}
		public WhileLoopContext whileLoop() {
			return getRuleContext(WhileLoopContext.class,0);
		}
		public DefineSingleLineFunctionContext defineSingleLineFunction() {
			return getRuleContext(DefineSingleLineFunctionContext.class,0);
		}
		public CallFunctionContext callFunction() {
			return getRuleContext(CallFunctionContext.class,0);
		}
		public PrintContext print() {
			return getRuleContext(PrintContext.class,0);
		}
		public InputContext input() {
			return getRuleContext(InputContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_statement);
		try {
			setState(79);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(49);
				match(COMMENT);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(50);
				blank();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(51);
				simpleAssignment();
				setState(52);
				match(EOL);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(54);
				arrayAssignment();
				setState(55);
				match(EOL);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(57);
				singleLineConditional();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(58);
				conditionalBlock();
				setState(59);
				match(EOL);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(61);
				forLoop();
				setState(62);
				match(EOL);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(64);
				whileLoop();
				setState(65);
				match(EOL);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(67);
				defineSingleLineFunction();
				setState(68);
				match(EOL);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(70);
				callFunction();
				setState(71);
				match(EOL);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(73);
				print();
				setState(74);
				match(EOL);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(76);
				input();
				setState(77);
				match(EOL);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleAssignmentContext extends ParserRuleContext {
		public Token IDENTIFIER;
		public List<Token> name = new ArrayList<Token>();
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(ArabicBASICParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(ArabicBASICParser.IDENTIFIER, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ArabicBASICParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ArabicBASICParser.COMMA, i);
		}
		public SimpleAssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleAssignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterSimpleAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitSimpleAssignment(this);
		}
	}

	public final SimpleAssignmentContext simpleAssignment() throws RecognitionException {
		SimpleAssignmentContext _localctx = new SimpleAssignmentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_simpleAssignment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(T__0);
			setState(82);
			((SimpleAssignmentContext)_localctx).IDENTIFIER = match(IDENTIFIER);
			((SimpleAssignmentContext)_localctx).name.add(((SimpleAssignmentContext)_localctx).IDENTIFIER);
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(83);
				match(COMMA);
				setState(84);
				((SimpleAssignmentContext)_localctx).IDENTIFIER = match(IDENTIFIER);
				((SimpleAssignmentContext)_localctx).name.add(((SimpleAssignmentContext)_localctx).IDENTIFIER);
				}
				}
				setState(89);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(90);
			match(T__1);
			setState(91);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayAssignmentContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(ArabicBASICParser.IDENTIFIER, 0); }
		public SubscriptContext subscript() {
			return getRuleContext(SubscriptContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArrayAssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayAssignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterArrayAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitArrayAssignment(this);
		}
	}

	public final ArrayAssignmentContext arrayAssignment() throws RecognitionException {
		ArrayAssignmentContext _localctx = new ArrayAssignmentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_arrayAssignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			match(IDENTIFIER);
			setState(94);
			match(T__2);
			setState(95);
			subscript();
			setState(96);
			match(T__3);
			setState(97);
			match(T__1);
			setState(98);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayCreationContext extends ParserRuleContext {
		public ArraySizeContext arraySize() {
			return getRuleContext(ArraySizeContext.class,0);
		}
		public ArrayCreationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterArrayCreation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitArrayCreation(this);
		}
	}

	public final ArrayCreationContext arrayCreation() throws RecognitionException {
		ArrayCreationContext _localctx = new ArrayCreationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_arrayCreation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			_la = _input.LA(1);
			if ( !(_la==T__4 || _la==T__5) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(101);
			match(T__6);
			setState(102);
			arraySize();
			setState(103);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalBlockContext extends ParserRuleContext {
		public BooleanExpressionContext booleanExpression;
		public List<BooleanExpressionContext> tests = new ArrayList<BooleanExpressionContext>();
		public List<TerminalNode> EOL() { return getTokens(ArabicBASICParser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(ArabicBASICParser.EOL, i);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public List<BooleanExpressionContext> booleanExpression() {
			return getRuleContexts(BooleanExpressionContext.class);
		}
		public BooleanExpressionContext booleanExpression(int i) {
			return getRuleContext(BooleanExpressionContext.class,i);
		}
		public ConditionalBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterConditionalBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitConditionalBlock(this);
		}
	}

	public final ConditionalBlockContext conditionalBlock() throws RecognitionException {
		ConditionalBlockContext _localctx = new ConditionalBlockContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_conditionalBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			_la = _input.LA(1);
			if ( !(_la==T__8 || _la==T__9) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(106);
			((ConditionalBlockContext)_localctx).booleanExpression = booleanExpression(0);
			((ConditionalBlockContext)_localctx).tests.add(((ConditionalBlockContext)_localctx).booleanExpression);
			setState(107);
			match(T__10);
			setState(108);
			match(EOL);
			setState(109);
			block();
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11 || _la==T__12) {
				{
				{
				setState(110);
				_la = _input.LA(1);
				if ( !(_la==T__11 || _la==T__12) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(111);
				((ConditionalBlockContext)_localctx).booleanExpression = booleanExpression(0);
				((ConditionalBlockContext)_localctx).tests.add(((ConditionalBlockContext)_localctx).booleanExpression);
				setState(112);
				match(T__10);
				setState(113);
				match(EOL);
				setState(114);
				block();
				}
				}
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__13) {
				{
				setState(121);
				match(T__13);
				setState(122);
				match(EOL);
				setState(123);
				block();
				}
			}

			setState(126);
			match(T__14);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SingleLineConditionalContext extends ParserRuleContext {
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public SingleLineConditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleLineConditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterSingleLineConditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitSingleLineConditional(this);
		}
	}

	public final SingleLineConditionalContext singleLineConditional() throws RecognitionException {
		SingleLineConditionalContext _localctx = new SingleLineConditionalContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_singleLineConditional);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			_la = _input.LA(1);
			if ( !(_la==T__8 || _la==T__9) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(129);
			booleanExpression(0);
			setState(130);
			match(T__10);
			setState(131);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForLoopContext extends ParserRuleContext {
		public Token control;
		public Token lower;
		public ExpressionContext upper;
		public Token step;
		public Token next;
		public TerminalNode EOL() { return getToken(ArabicBASICParser.EOL, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode NEXT_ADJ() { return getToken(ArabicBASICParser.NEXT_ADJ, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(ArabicBASICParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(ArabicBASICParser.IDENTIFIER, i);
		}
		public List<TerminalNode> INTEGER() { return getTokens(ArabicBASICParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(ArabicBASICParser.INTEGER, i);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForLoopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forLoop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterForLoop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitForLoop(this);
		}
	}

	public final ForLoopContext forLoop() throws RecognitionException {
		ForLoopContext _localctx = new ForLoopContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_forLoop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			match(T__15);
			setState(134);
			((ForLoopContext)_localctx).control = match(IDENTIFIER);
			setState(135);
			match(T__1);
			setState(136);
			((ForLoopContext)_localctx).lower = match(INTEGER);
			setState(137);
			match(T__16);
			setState(138);
			((ForLoopContext)_localctx).upper = expression(0);
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__17) {
				{
				setState(139);
				match(T__17);
				setState(140);
				match(T__1);
				setState(141);
				((ForLoopContext)_localctx).step = match(INTEGER);
				}
			}

			setState(144);
			match(EOL);
			setState(145);
			block();
			setState(146);
			((ForLoopContext)_localctx).next = match(IDENTIFIER);
			setState(147);
			match(NEXT_ADJ);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileLoopContext extends ParserRuleContext {
		public BooleanExpressionContext test;
		public TerminalNode EOL() { return getToken(ArabicBASICParser.EOL, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public WhileLoopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileLoop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterWhileLoop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitWhileLoop(this);
		}
	}

	public final WhileLoopContext whileLoop() throws RecognitionException {
		WhileLoopContext _localctx = new WhileLoopContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_whileLoop);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(T__18);
			setState(150);
			((WhileLoopContext)_localctx).test = booleanExpression(0);
			setState(151);
			match(EOL);
			setState(152);
			block();
			setState(153);
			match(T__19);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefineSingleLineFunctionContext extends ParserRuleContext {
		public Token funcName;
		public VariableContext arg;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(ArabicBASICParser.IDENTIFIER, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public DefineSingleLineFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defineSingleLineFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterDefineSingleLineFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitDefineSingleLineFunction(this);
		}
	}

	public final DefineSingleLineFunctionContext defineSingleLineFunction() throws RecognitionException {
		DefineSingleLineFunctionContext _localctx = new DefineSingleLineFunctionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_defineSingleLineFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			_la = _input.LA(1);
			if ( !(_la==T__20 || _la==T__21) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(156);
			((DefineSingleLineFunctionContext)_localctx).funcName = match(IDENTIFIER);
			setState(157);
			match(T__6);
			setState(158);
			((DefineSingleLineFunctionContext)_localctx).arg = variable();
			setState(159);
			match(T__7);
			setState(160);
			match(T__1);
			setState(161);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CallFunctionContext extends ParserRuleContext {
		public Token funcName;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(ArabicBASICParser.IDENTIFIER, 0); }
		public CallFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterCallFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitCallFunction(this);
		}
	}

	public final CallFunctionContext callFunction() throws RecognitionException {
		CallFunctionContext _localctx = new CallFunctionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_callFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			_la = _input.LA(1);
			if ( !(_la==T__22 || _la==T__23) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(164);
			((CallFunctionContext)_localctx).funcName = match(IDENTIFIER);
			setState(165);
			match(T__6);
			setState(166);
			variable();
			setState(167);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrintContext extends ParserRuleContext {
		public Token COMMA;
		public List<Token> spacer = new ArrayList<Token>();
		public Token s27;
		public Token s28;
		public Token _tset408;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ArabicBASICParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ArabicBASICParser.COMMA, i);
		}
		public PrintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_print; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterPrint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitPrint(this);
		}
	}

	public final PrintContext print() throws RecognitionException {
		PrintContext _localctx = new PrintContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_print);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			_la = _input.LA(1);
			if ( !(_la==T__24 || _la==T__25) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(170);
			expression(0);
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 27)) & ~0x3f) == 0 && ((1L << (_la - 27)) & 562949953421315L) != 0)) {
				{
				{
				setState(171);
				((PrintContext)_localctx)._tset408 = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 27)) & ~0x3f) == 0 && ((1L << (_la - 27)) & 562949953421315L) != 0)) ) {
					((PrintContext)_localctx)._tset408 = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				((PrintContext)_localctx).spacer.add(((PrintContext)_localctx)._tset408);
				setState(172);
				expression(0);
				}
				}
				setState(177);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InputContext extends ParserRuleContext {
		public Token prompt;
		public Token spacer;
		public Token IDENTIFIER;
		public List<Token> var = new ArrayList<Token>();
		public List<TerminalNode> IDENTIFIER() { return getTokens(ArabicBASICParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(ArabicBASICParser.IDENTIFIER, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ArabicBASICParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ArabicBASICParser.COMMA, i);
		}
		public TerminalNode STRING() { return getToken(ArabicBASICParser.STRING, 0); }
		public InputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterInput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitInput(this);
		}
	}

	public final InputContext input() throws RecognitionException {
		InputContext _localctx = new InputContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_input);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			_la = _input.LA(1);
			if ( !(_la==T__28 || _la==T__29) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(181);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING) {
				{
				setState(179);
				((InputContext)_localctx).prompt = match(STRING);
				{
				setState(180);
				((InputContext)_localctx).spacer = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 27)) & ~0x3f) == 0 && ((1L << (_la - 27)) & 562949953421315L) != 0)) ) {
					((InputContext)_localctx).spacer = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
			}

			setState(183);
			((InputContext)_localctx).IDENTIFIER = match(IDENTIFIER);
			((InputContext)_localctx).var.add(((InputContext)_localctx).IDENTIFIER);
			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(184);
				match(COMMA);
				setState(185);
				((InputContext)_localctx).IDENTIFIER = match(IDENTIFIER);
				((InputContext)_localctx).var.add(((InputContext)_localctx).IDENTIFIER);
				}
				}
				setState(190);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlankContext extends ParserRuleContext {
		public TerminalNode EOL() { return getToken(ArabicBASICParser.EOL, 0); }
		public List<TerminalNode> WS() { return getTokens(ArabicBASICParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(ArabicBASICParser.WS, i);
		}
		public BlankContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blank; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterBlank(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitBlank(this);
		}
	}

	public final BlankContext blank() throws RecognitionException {
		BlankContext _localctx = new BlankContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_blank);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(191);
				match(WS);
				}
				}
				setState(196);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(197);
			match(EOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StackEmptyFunctionContext extends ExpressionContext {
		public VariableContext stack;
		public TerminalNode STACK_EMPTY_PRED() { return getToken(ArabicBASICParser.STACK_EMPTY_PRED, 0); }
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public StackEmptyFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterStackEmptyFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitStackEmptyFunction(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringFunctionContext extends ExpressionContext {
		public Token name;
		public VariableContext variable;
		public List<VariableContext> arg = new ArrayList<VariableContext>();
		public List<VariableContext> variable() {
			return getRuleContexts(VariableContext.class);
		}
		public VariableContext variable(int i) {
			return getRuleContext(VariableContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(ArabicBASICParser.COMMA, 0); }
		public StringFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterStringFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitStringFunction(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MathFunctionContext extends ExpressionContext {
		public Token name;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public MathFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterMathFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitMathFunction(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayFactoryContext extends ExpressionContext {
		public ArrayCreationContext arrayCreation() {
			return getRuleContext(ArrayCreationContext.class,0);
		}
		public ArrayFactoryContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterArrayFactory(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitArrayFactory(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddSubContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public AddSubContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterAddSub(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitAddSub(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StackPushFunctionContext extends ExpressionContext {
		public VariableContext stack;
		public ExpressionContext value;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(ArabicBASICParser.COMMA, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StackPushFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterStackPushFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitStackPushFunction(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterUnary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitUnary(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NestedContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NestedContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterNested(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitNested(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExponentationContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExponentationContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterExponentation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitExponentation(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MulDivContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public MulDivContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterMulDiv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitMulDiv(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StackPopFunctionContext extends ExpressionContext {
		public VariableContext stack;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public StackPopFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterStackPopFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitStackPopFunction(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StackPeekFunctionContext extends ExpressionContext {
		public VariableContext stack;
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public StackPeekFunctionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterStackPeekFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitStackPeekFunction(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ExpressionContext {
		public CallFunctionContext callFunction() {
			return getRuleContext(CallFunctionContext.class,0);
		}
		public FunctionCallContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitFunctionCall(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StackFactoryContext extends ExpressionContext {
		public StackFactoryContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterStackFactory(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitStackFactory(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TermContext extends ExpressionContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TermContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitTerm(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ArrayAccessContext extends ExpressionContext {
		public TerminalNode IDENTIFIER() { return getToken(ArabicBASICParser.IDENTIFIER, 0); }
		public SubscriptContext subscript() {
			return getRuleContext(SubscriptContext.class,0);
		}
		public ArrayAccessContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterArrayAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitArrayAccess(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ModulusContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ModulusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterModulus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitModulus(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				_localctx = new MathFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(200);
				((MathFunctionContext)_localctx).name = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1097364144128L) != 0)) ) {
					((MathFunctionContext)_localctx).name = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(201);
				match(T__6);
				setState(202);
				expression(0);
				setState(203);
				match(T__7);
				}
				break;
			case 2:
				{
				_localctx = new StringFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(205);
				((StringFunctionContext)_localctx).name = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 69269232549888L) != 0)) ) {
					((StringFunctionContext)_localctx).name = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(206);
				match(T__6);
				setState(207);
				((StringFunctionContext)_localctx).variable = variable();
				((StringFunctionContext)_localctx).arg.add(((StringFunctionContext)_localctx).variable);
				setState(210);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(208);
					match(COMMA);
					setState(209);
					((StringFunctionContext)_localctx).variable = variable();
					((StringFunctionContext)_localctx).arg.add(((StringFunctionContext)_localctx).variable);
					}
				}

				setState(212);
				match(T__7);
				}
				break;
			case 3:
				{
				_localctx = new StackFactoryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(214);
				match(T__45);
				setState(215);
				match(T__6);
				setState(216);
				match(T__7);
				}
				break;
			case 4:
				{
				_localctx = new StackPushFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(217);
				_la = _input.LA(1);
				if ( !(_la==T__46 || _la==T__47) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(218);
				match(T__6);
				setState(219);
				((StackPushFunctionContext)_localctx).stack = variable();
				{
				setState(220);
				match(COMMA);
				setState(221);
				((StackPushFunctionContext)_localctx).value = expression(0);
				}
				setState(223);
				match(T__7);
				}
				break;
			case 5:
				{
				_localctx = new StackPopFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(225);
				_la = _input.LA(1);
				if ( !(_la==T__48 || _la==T__49) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(226);
				match(T__6);
				setState(227);
				((StackPopFunctionContext)_localctx).stack = variable();
				setState(228);
				match(T__7);
				}
				break;
			case 6:
				{
				_localctx = new StackPeekFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(230);
				_la = _input.LA(1);
				if ( !(_la==T__50 || _la==T__51) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(231);
				match(T__6);
				setState(232);
				((StackPeekFunctionContext)_localctx).stack = variable();
				setState(233);
				match(T__7);
				}
				break;
			case 7:
				{
				_localctx = new StackEmptyFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(235);
				match(STACK_EMPTY_PRED);
				setState(236);
				match(T__6);
				setState(237);
				((StackEmptyFunctionContext)_localctx).stack = variable();
				setState(238);
				match(T__7);
				}
				break;
			case 8:
				{
				_localctx = new ArrayAccessContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(240);
				match(IDENTIFIER);
				setState(241);
				match(T__2);
				setState(242);
				subscript();
				setState(243);
				match(T__3);
				}
				break;
			case 9:
				{
				_localctx = new UnaryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(245);
				match(T__52);
				setState(246);
				expression(9);
				}
				break;
			case 10:
				{
				_localctx = new ArrayFactoryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(247);
				arrayCreation();
				}
				break;
			case 11:
				{
				_localctx = new FunctionCallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(248);
				callFunction();
				}
				break;
			case 12:
				{
				_localctx = new TermContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(249);
				variable();
				}
				break;
			case 13:
				{
				_localctx = new NestedContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(250);
				match(T__6);
				setState(251);
				expression(0);
				setState(252);
				match(T__7);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(270);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(268);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
					case 1:
						{
						_localctx = new ExponentationContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(256);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(257);
						match(T__53);
						setState(258);
						expression(8);
						}
						break;
					case 2:
						{
						_localctx = new ModulusContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(259);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(260);
						((ModulusContext)_localctx).op = match(T__54);
						setState(261);
						expression(8);
						}
						break;
					case 3:
						{
						_localctx = new MulDivContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(262);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(263);
						((MulDivContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__55 || _la==T__56) ) {
							((MulDivContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(264);
						expression(7);
						}
						break;
					case 4:
						{
						_localctx = new AddSubContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(265);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(266);
						((AddSubContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__52 || _la==T__57) ) {
							((AddSubContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(267);
						expression(6);
						}
						break;
					}
					} 
				}
				setState(272);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SubscriptContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(ArabicBASICParser.INTEGER, 0); }
		public TerminalNode IDENTIFIER() { return getToken(ArabicBASICParser.IDENTIFIER, 0); }
		public SubscriptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subscript; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterSubscript(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitSubscript(this);
		}
	}

	public final SubscriptContext subscript() throws RecognitionException {
		SubscriptContext _localctx = new SubscriptContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_subscript);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			_la = _input.LA(1);
			if ( !(_la==IDENTIFIER || _la==INTEGER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArraySizeContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArraySizeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arraySize; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterArraySize(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitArraySize(this);
		}
	}

	public final ArraySizeContext arraySize() throws RecognitionException {
		ArraySizeContext _localctx = new ArraySizeContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_arraySize);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(275);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BooleanExpressionContext extends ParserRuleContext {
		public BooleanExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanExpression; }
	 
		public BooleanExpressionContext() { }
		public void copyFrom(BooleanExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AtomicBooleanContext extends BooleanExpressionContext {
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public AtomicBooleanContext(BooleanExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterAtomicBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitAtomicBoolean(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionBooleanContext extends BooleanExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionBooleanContext(BooleanExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterExpressionBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitExpressionBoolean(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LogicalAndContext extends BooleanExpressionContext {
		public List<BooleanExpressionContext> booleanExpression() {
			return getRuleContexts(BooleanExpressionContext.class);
		}
		public BooleanExpressionContext booleanExpression(int i) {
			return getRuleContext(BooleanExpressionContext.class,i);
		}
		public LogicalAndContext(BooleanExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterLogicalAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitLogicalAnd(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ComparitiveBooleanContext extends BooleanExpressionContext {
		public Token comp;
		public List<BooleanExpressionContext> booleanExpression() {
			return getRuleContexts(BooleanExpressionContext.class);
		}
		public BooleanExpressionContext booleanExpression(int i) {
			return getRuleContext(BooleanExpressionContext.class,i);
		}
		public ComparitiveBooleanContext(BooleanExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterComparitiveBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitComparitiveBoolean(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LogicalOrContext extends BooleanExpressionContext {
		public List<BooleanExpressionContext> booleanExpression() {
			return getRuleContexts(BooleanExpressionContext.class);
		}
		public BooleanExpressionContext booleanExpression(int i) {
			return getRuleContext(BooleanExpressionContext.class,i);
		}
		public LogicalOrContext(BooleanExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterLogicalOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitLogicalOr(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NegatingBooleanContext extends BooleanExpressionContext {
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public NegatingBooleanContext(BooleanExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterNegatingBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitNegatingBoolean(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NestedBooleanContext extends BooleanExpressionContext {
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public NestedBooleanContext(BooleanExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterNestedBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitNestedBoolean(this);
		}
	}

	public final BooleanExpressionContext booleanExpression() throws RecognitionException {
		return booleanExpression(0);
	}

	private BooleanExpressionContext booleanExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		BooleanExpressionContext _localctx = new BooleanExpressionContext(_ctx, _parentState);
		BooleanExpressionContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_booleanExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(286);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				_localctx = new NegatingBooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(278);
				match(T__63);
				setState(279);
				booleanExpression(6);
				}
				break;
			case 2:
				{
				_localctx = new ExpressionBooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(280);
				expression(0);
				}
				break;
			case 3:
				{
				_localctx = new AtomicBooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(281);
				variable();
				}
				break;
			case 4:
				{
				_localctx = new NestedBooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(282);
				match(T__6);
				setState(283);
				booleanExpression(0);
				setState(284);
				match(T__7);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(299);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(297);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
					case 1:
						{
						_localctx = new ComparitiveBooleanContext(new BooleanExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_booleanExpression);
						setState(288);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(289);
						((ComparitiveBooleanContext)_localctx).comp = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -576460752303423484L) != 0)) ) {
							((ComparitiveBooleanContext)_localctx).comp = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(290);
						booleanExpression(8);
						}
						break;
					case 2:
						{
						_localctx = new LogicalAndContext(new BooleanExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_booleanExpression);
						setState(291);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(292);
						match(T__64);
						setState(293);
						booleanExpression(6);
						}
						break;
					case 3:
						{
						_localctx = new LogicalOrContext(new BooleanExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_booleanExpression);
						setState(294);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(295);
						_la = _input.LA(1);
						if ( !(((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 7L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(296);
						booleanExpression(5);
						}
						break;
					}
					} 
				}
				setState(301);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends ParserRuleContext {
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
	 
		public VariableContext() { }
		public void copyFrom(VariableContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolContext extends VariableContext {
		public BoolContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitBool(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NameContext extends VariableContext {
		public TerminalNode IDENTIFIER() { return getToken(ArabicBASICParser.IDENTIFIER, 0); }
		public NameContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitName(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumericContext extends VariableContext {
		public TerminalNode INTEGER() { return getToken(ArabicBASICParser.INTEGER, 0); }
		public TerminalNode REAL() { return getToken(ArabicBASICParser.REAL, 0); }
		public NumericContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterNumeric(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitNumeric(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TextContext extends VariableContext {
		public TerminalNode STRING() { return getToken(ArabicBASICParser.STRING, 0); }
		public TextContext(VariableContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).enterText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArabicBASICListener ) ((ArabicBASICListener)listener).exitText(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_variable);
		int _la;
		try {
			setState(306);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				_localctx = new NameContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(302);
				match(IDENTIFIER);
				}
				break;
			case REAL:
			case INTEGER:
				_localctx = new NumericContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(303);
				_la = _input.LA(1);
				if ( !(_la==REAL || _la==INTEGER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case STRING:
				_localctx = new TextContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(304);
				match(STRING);
				}
				break;
			case T__68:
			case T__69:
				_localctx = new BoolContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(305);
				_la = _input.LA(1);
				if ( !(_la==T__68 || _la==T__69) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 15:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 18:
			return booleanExpression_sempred((BooleanExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 8);
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		case 3:
			return precpred(_ctx, 5);
		}
		return true;
	}
	private boolean booleanExpression_sempred(BooleanExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 7);
		case 5:
			return precpred(_ctx, 5);
		case 6:
			return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001Q\u0135\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0005\u0001-\b\u0001\n\u0001\f\u00010\t\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002P\b\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003V\b\u0003"+
		"\n\u0003\f\u0003Y\t\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006u\b\u0006"+
		"\n\u0006\f\u0006x\t\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006"+
		"}\b\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0003\b\u008f\b\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0005\f\u00ae\b\f\n\f\f\f\u00b1\t\f\u0001\r\u0001\r\u0001\r"+
		"\u0003\r\u00b6\b\r\u0001\r\u0001\r\u0001\r\u0005\r\u00bb\b\r\n\r\f\r\u00be"+
		"\t\r\u0001\u000e\u0005\u000e\u00c1\b\u000e\n\u000e\f\u000e\u00c4\t\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u00d3\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u00ff\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f"+
		"\u010d\b\u000f\n\u000f\f\u000f\u0110\t\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u011f"+
		"\b\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u012a\b\u0012\n"+
		"\u0012\f\u0012\u012d\t\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0003\u0013\u0133\b\u0013\u0001\u0013\u0000\u0002\u001e$\u0014"+
		"\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a"+
		"\u001c\u001e \"$&\u0000\u0014\u0001\u0000\u0005\u0006\u0001\u0000\t\n"+
		"\u0001\u0000\f\r\u0001\u0000\u0015\u0016\u0001\u0000\u0017\u0018\u0001"+
		"\u0000\u0019\u001a\u0002\u0000\u001b\u001cLL\u0001\u0000\u001d\u001e\u0001"+
		"\u0000\u001f\'\u0001\u0000(-\u0001\u0000/0\u0001\u000012\u0001\u00003"+
		"4\u0001\u000089\u0002\u000055::\u0002\u0000KKNN\u0002\u0000\u0002\u0002"+
		";?\u0001\u0000BD\u0001\u0000MN\u0001\u0000EF\u014e\u0000(\u0001\u0000"+
		"\u0000\u0000\u0002.\u0001\u0000\u0000\u0000\u0004O\u0001\u0000\u0000\u0000"+
		"\u0006Q\u0001\u0000\u0000\u0000\b]\u0001\u0000\u0000\u0000\nd\u0001\u0000"+
		"\u0000\u0000\fi\u0001\u0000\u0000\u0000\u000e\u0080\u0001\u0000\u0000"+
		"\u0000\u0010\u0085\u0001\u0000\u0000\u0000\u0012\u0095\u0001\u0000\u0000"+
		"\u0000\u0014\u009b\u0001\u0000\u0000\u0000\u0016\u00a3\u0001\u0000\u0000"+
		"\u0000\u0018\u00a9\u0001\u0000\u0000\u0000\u001a\u00b2\u0001\u0000\u0000"+
		"\u0000\u001c\u00c2\u0001\u0000\u0000\u0000\u001e\u00fe\u0001\u0000\u0000"+
		"\u0000 \u0111\u0001\u0000\u0000\u0000\"\u0113\u0001\u0000\u0000\u0000"+
		"$\u011e\u0001\u0000\u0000\u0000&\u0132\u0001\u0000\u0000\u0000()\u0003"+
		"\u0002\u0001\u0000)*\u0005\u0000\u0000\u0001*\u0001\u0001\u0000\u0000"+
		"\u0000+-\u0003\u0004\u0002\u0000,+\u0001\u0000\u0000\u0000-0\u0001\u0000"+
		"\u0000\u0000.,\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000/\u0003"+
		"\u0001\u0000\u0000\u00000.\u0001\u0000\u0000\u00001P\u0005G\u0000\u0000"+
		"2P\u0003\u001c\u000e\u000034\u0003\u0006\u0003\u000045\u0005O\u0000\u0000"+
		"5P\u0001\u0000\u0000\u000067\u0003\b\u0004\u000078\u0005O\u0000\u0000"+
		"8P\u0001\u0000\u0000\u00009P\u0003\u000e\u0007\u0000:;\u0003\f\u0006\u0000"+
		";<\u0005O\u0000\u0000<P\u0001\u0000\u0000\u0000=>\u0003\u0010\b\u0000"+
		">?\u0005O\u0000\u0000?P\u0001\u0000\u0000\u0000@A\u0003\u0012\t\u0000"+
		"AB\u0005O\u0000\u0000BP\u0001\u0000\u0000\u0000CD\u0003\u0014\n\u0000"+
		"DE\u0005O\u0000\u0000EP\u0001\u0000\u0000\u0000FG\u0003\u0016\u000b\u0000"+
		"GH\u0005O\u0000\u0000HP\u0001\u0000\u0000\u0000IJ\u0003\u0018\f\u0000"+
		"JK\u0005O\u0000\u0000KP\u0001\u0000\u0000\u0000LM\u0003\u001a\r\u0000"+
		"MN\u0005O\u0000\u0000NP\u0001\u0000\u0000\u0000O1\u0001\u0000\u0000\u0000"+
		"O2\u0001\u0000\u0000\u0000O3\u0001\u0000\u0000\u0000O6\u0001\u0000\u0000"+
		"\u0000O9\u0001\u0000\u0000\u0000O:\u0001\u0000\u0000\u0000O=\u0001\u0000"+
		"\u0000\u0000O@\u0001\u0000\u0000\u0000OC\u0001\u0000\u0000\u0000OF\u0001"+
		"\u0000\u0000\u0000OI\u0001\u0000\u0000\u0000OL\u0001\u0000\u0000\u0000"+
		"P\u0005\u0001\u0000\u0000\u0000QR\u0005\u0001\u0000\u0000RW\u0005K\u0000"+
		"\u0000ST\u0005L\u0000\u0000TV\u0005K\u0000\u0000US\u0001\u0000\u0000\u0000"+
		"VY\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000WX\u0001\u0000\u0000"+
		"\u0000XZ\u0001\u0000\u0000\u0000YW\u0001\u0000\u0000\u0000Z[\u0005\u0002"+
		"\u0000\u0000[\\\u0003\u001e\u000f\u0000\\\u0007\u0001\u0000\u0000\u0000"+
		"]^\u0005K\u0000\u0000^_\u0005\u0003\u0000\u0000_`\u0003 \u0010\u0000`"+
		"a\u0005\u0004\u0000\u0000ab\u0005\u0002\u0000\u0000bc\u0003\u001e\u000f"+
		"\u0000c\t\u0001\u0000\u0000\u0000de\u0007\u0000\u0000\u0000ef\u0005\u0007"+
		"\u0000\u0000fg\u0003\"\u0011\u0000gh\u0005\b\u0000\u0000h\u000b\u0001"+
		"\u0000\u0000\u0000ij\u0007\u0001\u0000\u0000jk\u0003$\u0012\u0000kl\u0005"+
		"\u000b\u0000\u0000lm\u0005O\u0000\u0000mv\u0003\u0002\u0001\u0000no\u0007"+
		"\u0002\u0000\u0000op\u0003$\u0012\u0000pq\u0005\u000b\u0000\u0000qr\u0005"+
		"O\u0000\u0000rs\u0003\u0002\u0001\u0000su\u0001\u0000\u0000\u0000tn\u0001"+
		"\u0000\u0000\u0000ux\u0001\u0000\u0000\u0000vt\u0001\u0000\u0000\u0000"+
		"vw\u0001\u0000\u0000\u0000w|\u0001\u0000\u0000\u0000xv\u0001\u0000\u0000"+
		"\u0000yz\u0005\u000e\u0000\u0000z{\u0005O\u0000\u0000{}\u0003\u0002\u0001"+
		"\u0000|y\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}~\u0001\u0000"+
		"\u0000\u0000~\u007f\u0005\u000f\u0000\u0000\u007f\r\u0001\u0000\u0000"+
		"\u0000\u0080\u0081\u0007\u0001\u0000\u0000\u0081\u0082\u0003$\u0012\u0000"+
		"\u0082\u0083\u0005\u000b\u0000\u0000\u0083\u0084\u0003\u0004\u0002\u0000"+
		"\u0084\u000f\u0001\u0000\u0000\u0000\u0085\u0086\u0005\u0010\u0000\u0000"+
		"\u0086\u0087\u0005K\u0000\u0000\u0087\u0088\u0005\u0002\u0000\u0000\u0088"+
		"\u0089\u0005N\u0000\u0000\u0089\u008a\u0005\u0011\u0000\u0000\u008a\u008e"+
		"\u0003\u001e\u000f\u0000\u008b\u008c\u0005\u0012\u0000\u0000\u008c\u008d"+
		"\u0005\u0002\u0000\u0000\u008d\u008f\u0005N\u0000\u0000\u008e\u008b\u0001"+
		"\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000\u0000\u008f\u0090\u0001"+
		"\u0000\u0000\u0000\u0090\u0091\u0005O\u0000\u0000\u0091\u0092\u0003\u0002"+
		"\u0001\u0000\u0092\u0093\u0005K\u0000\u0000\u0093\u0094\u0005I\u0000\u0000"+
		"\u0094\u0011\u0001\u0000\u0000\u0000\u0095\u0096\u0005\u0013\u0000\u0000"+
		"\u0096\u0097\u0003$\u0012\u0000\u0097\u0098\u0005O\u0000\u0000\u0098\u0099"+
		"\u0003\u0002\u0001\u0000\u0099\u009a\u0005\u0014\u0000\u0000\u009a\u0013"+
		"\u0001\u0000\u0000\u0000\u009b\u009c\u0007\u0003\u0000\u0000\u009c\u009d"+
		"\u0005K\u0000\u0000\u009d\u009e\u0005\u0007\u0000\u0000\u009e\u009f\u0003"+
		"&\u0013\u0000\u009f\u00a0\u0005\b\u0000\u0000\u00a0\u00a1\u0005\u0002"+
		"\u0000\u0000\u00a1\u00a2\u0003\u001e\u000f\u0000\u00a2\u0015\u0001\u0000"+
		"\u0000\u0000\u00a3\u00a4\u0007\u0004\u0000\u0000\u00a4\u00a5\u0005K\u0000"+
		"\u0000\u00a5\u00a6\u0005\u0007\u0000\u0000\u00a6\u00a7\u0003&\u0013\u0000"+
		"\u00a7\u00a8\u0005\b\u0000\u0000\u00a8\u0017\u0001\u0000\u0000\u0000\u00a9"+
		"\u00aa\u0007\u0005\u0000\u0000\u00aa\u00af\u0003\u001e\u000f\u0000\u00ab"+
		"\u00ac\u0007\u0006\u0000\u0000\u00ac\u00ae\u0003\u001e\u000f\u0000\u00ad"+
		"\u00ab\u0001\u0000\u0000\u0000\u00ae\u00b1\u0001\u0000\u0000\u0000\u00af"+
		"\u00ad\u0001\u0000\u0000\u0000\u00af\u00b0\u0001\u0000\u0000\u0000\u00b0"+
		"\u0019\u0001\u0000\u0000\u0000\u00b1\u00af\u0001\u0000\u0000\u0000\u00b2"+
		"\u00b5\u0007\u0007\u0000\u0000\u00b3\u00b4\u0005H\u0000\u0000\u00b4\u00b6"+
		"\u0007\u0006\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b5\u00b6"+
		"\u0001\u0000\u0000\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7\u00bc"+
		"\u0005K\u0000\u0000\u00b8\u00b9\u0005L\u0000\u0000\u00b9\u00bb\u0005K"+
		"\u0000\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00bb\u00be\u0001\u0000"+
		"\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000"+
		"\u0000\u0000\u00bd\u001b\u0001\u0000\u0000\u0000\u00be\u00bc\u0001\u0000"+
		"\u0000\u0000\u00bf\u00c1\u0005P\u0000\u0000\u00c0\u00bf\u0001\u0000\u0000"+
		"\u0000\u00c1\u00c4\u0001\u0000\u0000\u0000\u00c2\u00c0\u0001\u0000\u0000"+
		"\u0000\u00c2\u00c3\u0001\u0000\u0000\u0000\u00c3\u00c5\u0001\u0000\u0000"+
		"\u0000\u00c4\u00c2\u0001\u0000\u0000\u0000\u00c5\u00c6\u0005O\u0000\u0000"+
		"\u00c6\u001d\u0001\u0000\u0000\u0000\u00c7\u00c8\u0006\u000f\uffff\uffff"+
		"\u0000\u00c8\u00c9\u0007\b\u0000\u0000\u00c9\u00ca\u0005\u0007\u0000\u0000"+
		"\u00ca\u00cb\u0003\u001e\u000f\u0000\u00cb\u00cc\u0005\b\u0000\u0000\u00cc"+
		"\u00ff\u0001\u0000\u0000\u0000\u00cd\u00ce\u0007\t\u0000\u0000\u00ce\u00cf"+
		"\u0005\u0007\u0000\u0000\u00cf\u00d2\u0003&\u0013\u0000\u00d0\u00d1\u0005"+
		"L\u0000\u0000\u00d1\u00d3\u0003&\u0013\u0000\u00d2\u00d0\u0001\u0000\u0000"+
		"\u0000\u00d2\u00d3\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000"+
		"\u0000\u00d4\u00d5\u0005\b\u0000\u0000\u00d5\u00ff\u0001\u0000\u0000\u0000"+
		"\u00d6\u00d7\u0005.\u0000\u0000\u00d7\u00d8\u0005\u0007\u0000\u0000\u00d8"+
		"\u00ff\u0005\b\u0000\u0000\u00d9\u00da\u0007\n\u0000\u0000\u00da\u00db"+
		"\u0005\u0007\u0000\u0000\u00db\u00dc\u0003&\u0013\u0000\u00dc\u00dd\u0005"+
		"L\u0000\u0000\u00dd\u00de\u0003\u001e\u000f\u0000\u00de\u00df\u0001\u0000"+
		"\u0000\u0000\u00df\u00e0\u0005\b\u0000\u0000\u00e0\u00ff\u0001\u0000\u0000"+
		"\u0000\u00e1\u00e2\u0007\u000b\u0000\u0000\u00e2\u00e3\u0005\u0007\u0000"+
		"\u0000\u00e3\u00e4\u0003&\u0013\u0000\u00e4\u00e5\u0005\b\u0000\u0000"+
		"\u00e5\u00ff\u0001\u0000\u0000\u0000\u00e6\u00e7\u0007\f\u0000\u0000\u00e7"+
		"\u00e8\u0005\u0007\u0000\u0000\u00e8\u00e9\u0003&\u0013\u0000\u00e9\u00ea"+
		"\u0005\b\u0000\u0000\u00ea\u00ff\u0001\u0000\u0000\u0000\u00eb\u00ec\u0005"+
		"J\u0000\u0000\u00ec\u00ed\u0005\u0007\u0000\u0000\u00ed\u00ee\u0003&\u0013"+
		"\u0000\u00ee\u00ef\u0005\b\u0000\u0000\u00ef\u00ff\u0001\u0000\u0000\u0000"+
		"\u00f0\u00f1\u0005K\u0000\u0000\u00f1\u00f2\u0005\u0003\u0000\u0000\u00f2"+
		"\u00f3\u0003 \u0010\u0000\u00f3\u00f4\u0005\u0004\u0000\u0000\u00f4\u00ff"+
		"\u0001\u0000\u0000\u0000\u00f5\u00f6\u00055\u0000\u0000\u00f6\u00ff\u0003"+
		"\u001e\u000f\t\u00f7\u00ff\u0003\n\u0005\u0000\u00f8\u00ff\u0003\u0016"+
		"\u000b\u0000\u00f9\u00ff\u0003&\u0013\u0000\u00fa\u00fb\u0005\u0007\u0000"+
		"\u0000\u00fb\u00fc\u0003\u001e\u000f\u0000\u00fc\u00fd\u0005\b\u0000\u0000"+
		"\u00fd\u00ff\u0001\u0000\u0000\u0000\u00fe\u00c7\u0001\u0000\u0000\u0000"+
		"\u00fe\u00cd\u0001\u0000\u0000\u0000\u00fe\u00d6\u0001\u0000\u0000\u0000"+
		"\u00fe\u00d9\u0001\u0000\u0000\u0000\u00fe\u00e1\u0001\u0000\u0000\u0000"+
		"\u00fe\u00e6\u0001\u0000\u0000\u0000\u00fe\u00eb\u0001\u0000\u0000\u0000"+
		"\u00fe\u00f0\u0001\u0000\u0000\u0000\u00fe\u00f5\u0001\u0000\u0000\u0000"+
		"\u00fe\u00f7\u0001\u0000\u0000\u0000\u00fe\u00f8\u0001\u0000\u0000\u0000"+
		"\u00fe\u00f9\u0001\u0000\u0000\u0000\u00fe\u00fa\u0001\u0000\u0000\u0000"+
		"\u00ff\u010e\u0001\u0000\u0000\u0000\u0100\u0101\n\b\u0000\u0000\u0101"+
		"\u0102\u00056\u0000\u0000\u0102\u010d\u0003\u001e\u000f\b\u0103\u0104"+
		"\n\u0007\u0000\u0000\u0104\u0105\u00057\u0000\u0000\u0105\u010d\u0003"+
		"\u001e\u000f\b\u0106\u0107\n\u0006\u0000\u0000\u0107\u0108\u0007\r\u0000"+
		"\u0000\u0108\u010d\u0003\u001e\u000f\u0007\u0109\u010a\n\u0005\u0000\u0000"+
		"\u010a\u010b\u0007\u000e\u0000\u0000\u010b\u010d\u0003\u001e\u000f\u0006"+
		"\u010c\u0100\u0001\u0000\u0000\u0000\u010c\u0103\u0001\u0000\u0000\u0000"+
		"\u010c\u0106\u0001\u0000\u0000\u0000\u010c\u0109\u0001\u0000\u0000\u0000"+
		"\u010d\u0110\u0001\u0000\u0000\u0000\u010e\u010c\u0001\u0000\u0000\u0000"+
		"\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u001f\u0001\u0000\u0000\u0000"+
		"\u0110\u010e\u0001\u0000\u0000\u0000\u0111\u0112\u0007\u000f\u0000\u0000"+
		"\u0112!\u0001\u0000\u0000\u0000\u0113\u0114\u0003\u001e\u000f\u0000\u0114"+
		"#\u0001\u0000\u0000\u0000\u0115\u0116\u0006\u0012\uffff\uffff\u0000\u0116"+
		"\u0117\u0005@\u0000\u0000\u0117\u011f\u0003$\u0012\u0006\u0118\u011f\u0003"+
		"\u001e\u000f\u0000\u0119\u011f\u0003&\u0013\u0000\u011a\u011b\u0005\u0007"+
		"\u0000\u0000\u011b\u011c\u0003$\u0012\u0000\u011c\u011d\u0005\b\u0000"+
		"\u0000\u011d\u011f\u0001\u0000\u0000\u0000\u011e\u0115\u0001\u0000\u0000"+
		"\u0000\u011e\u0118\u0001\u0000\u0000\u0000\u011e\u0119\u0001\u0000\u0000"+
		"\u0000\u011e\u011a\u0001\u0000\u0000\u0000\u011f\u012b\u0001\u0000\u0000"+
		"\u0000\u0120\u0121\n\u0007\u0000\u0000\u0121\u0122\u0007\u0010\u0000\u0000"+
		"\u0122\u012a\u0003$\u0012\b\u0123\u0124\n\u0005\u0000\u0000\u0124\u0125"+
		"\u0005A\u0000\u0000\u0125\u012a\u0003$\u0012\u0006\u0126\u0127\n\u0004"+
		"\u0000\u0000\u0127\u0128\u0007\u0011\u0000\u0000\u0128\u012a\u0003$\u0012"+
		"\u0005\u0129\u0120\u0001\u0000\u0000\u0000\u0129\u0123\u0001\u0000\u0000"+
		"\u0000\u0129\u0126\u0001\u0000\u0000\u0000\u012a\u012d\u0001\u0000\u0000"+
		"\u0000\u012b\u0129\u0001\u0000\u0000\u0000\u012b\u012c\u0001\u0000\u0000"+
		"\u0000\u012c%\u0001\u0000\u0000\u0000\u012d\u012b\u0001\u0000\u0000\u0000"+
		"\u012e\u0133\u0005K\u0000\u0000\u012f\u0133\u0007\u0012\u0000\u0000\u0130"+
		"\u0133\u0005H\u0000\u0000\u0131\u0133\u0007\u0013\u0000\u0000\u0132\u012e"+
		"\u0001\u0000\u0000\u0000\u0132\u012f\u0001\u0000\u0000\u0000\u0132\u0130"+
		"\u0001\u0000\u0000\u0000\u0132\u0131\u0001\u0000\u0000\u0000\u0133\'\u0001"+
		"\u0000\u0000\u0000\u0012.OWv|\u008e\u00af\u00b5\u00bc\u00c2\u00d2\u00fe"+
		"\u010c\u010e\u011e\u0129\u012b\u0132";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}