%{                                                            /* -*- C++ -*- */
//#include "../ast/AST.h"
#include "parser.hh"
#include "scanner.hh"
#include "driver.hh"

/*  Defines some macros to update locations */


#define STEP()                                      \
  do {                                              \
    yylloc->step ();                      \
  } while (0)

#define COL(Col)				                            \
  yylloc->columns (Col)

#define LINE(Line)				                          \
  do{						                                    \
    yylloc->lines (Line);		              \
 } while (0)

#define YY_USER_ACTION				                      \
  COL(yyleng);


typedef parse::Parser::token token;
typedef parse::Parser::token_type token_type;

#define yyterminate() return token::TOK_EOF

%}

%option debug
%option c++
%option noyywrap
%option never-interactive
%option yylineno
%option nounput
%option batch
%option prefix="parse"

/*
%option stack
*/

/* Abbreviations.  */

blank   [ \t]+
eol     [\n\r]+

%%

 /* The rules.  */
%{
  STEP();
%}

\/\/[^\n]* ;

"int" {yylval->type = AST::basicType::integer; return token::TYPE;}
"char" {yylval->type = AST::basicType::character; return token::TYPE;}

"<" {yylval->cmp = AST::cmp::LS; return token::CMP;}
"==" {yylval->cmp = AST::cmp::EQ; return token::CMP;}

"=" return token::ASSIGN;
"(" return token::LP;
")" return token::RP;
"{" return token::LB;
"}" return token::RB;
"[" return token::LF;
"]" return token::RF;
"*" {yylval->op = AST::binOP::MUL;return token::MUL;}
"+" {yylval->op = AST::binOP::ADD;return token::ADD;}
"/" {yylval->op = AST::binOP::DIV;return token::DIV;}
"-" {yylval->op = AST::binOP::SUBS;return token::SUBS;}
"%" {yylval->op = AST::binOP::MOD; return token::MOD;}
"#" return token::SP;
"&" return token::AD;
";" return token::SEMI;
"," return token::COMMA;

"func" return token::FUNC; //func foo(type a, type b, ...) {return expr;}
"return" return token::RETURN;
"print" return token::PRINT;
"if" return token::IF;
"else" return token::ELSE;
"while" return token::WHILE;
"do" return token::DO;

[a-zA-Z_][a-zA-Z0-9_]* {
  yylval->id = new AST::identifyNode(std::string(yytext, yyleng));
  return token::IDENTIFY;
  }
[+-]?[0-9][0-9]* {
  yylval->integer = new AST::integerNode(atoi(std::string(yytext, yyleng).c_str()));
  return token::INTEGER;
  }

{blank}       STEP();

{eol}         LINE(yyleng);

.             {
                std::cerr << *yylloc << " Unexpected token : "
                                              << *yytext << std::endl;
                driver.error_ = (driver.error_ == 127 ? 127
                                : driver.error_ + 1);
                STEP ();
              }

%%


/*

   CUSTOM C++ CODE

*/

namespace parse
{

    Scanner::Scanner()
    : parseFlexLexer()
    {
    }

    Scanner::~Scanner()
    {
    }

    void Scanner::set_debug(bool b)
    {
        yy_flex_debug = b;
    }
}

#ifdef yylex
# undef yylex
#endif

int parseFlexLexer::yylex()
{
  std::cerr << "call parsepitFlexLexer::yylex()!" << std::endl;
  return 0;
}
