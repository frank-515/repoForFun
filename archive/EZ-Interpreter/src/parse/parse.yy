%{     /* PARSER */
#include "parser.hh"
#include "scanner.hh"

#define yylex driver.scanner_->yylex
%}

%code requires
{

  #include "../ast/AST.h"

  extern AST::ExprNode *root;

  #include <iostream>
  #include <string>
  #include "driver.hh"
  #include "location.hh"
  #include "position.hh"
}

%code provides
{
  namespace parse
  {
    // Forward declaration of the Driver class
    class Driver;

    inline void
    yyerror (const char* msg)
    {
      std::cerr << msg << std::endl;
    }
  }
}



%require "2.4"
%language "C++"
%locations
%defines
%debug
%define api.namespace {parse}
%define api.parser.class {Parser}
%parse-param {Driver &driver}
%lex-param {Driver &driver}
%define parse.error verbose

%union
{
  AST::Node *node;
  AST::StatementNode *stmt;
  AST::blockNode *block;
  AST::binOP op;
  AST::ExprNode *expr;
  AST::assignNode *assign;
  AST::identifyNode *id;
  AST::integerNode *integer;
  AST::ifStatementNode *ifNode;
  std::vector<AST::StatementNode*> *stmts;
  AST::printStatementNode *print;
  AST::whileStatementNode *whilestmt;
  AST::dowhileStatementNode *dowhile;
  AST::Program *program;
  AST::basicType type;
  AST::declStatementNode *decl;
  AST::cmp cmp;
  AST::cmpExprNode *cmpstmt;
  AST::arrayAccessExprNode *arracs;
  AST::arrayAssignNode *arrass;
  AST::getAddressOp *getAddrOp;
  AST::getValueOp *getValOp;
  AST::paraList *paraList;
  AST::Function *function;
  AST::functionDef *funcDef;
  AST::argumentList *arguList;
  AST::callStmt *callStmt;
  AST::returnStmt *retStmt;

 /* YYLTYPE */

}

/* Tokens */
%token TOK_EOF 0
%token ASSIGN LP RP LB RB LF RF SEMI SP AD COMMA
%token<op> MUL ADD DIV SUBS MOD
%token IF ELSE WHILE PRINT DO FUNC RETURN
%token<id> IDENTIFY 
%token<integer> INTEGER
%token<type> TYPE
%token<cmp> CMP

/* Non-terminals */
%type<stmt> stmt
%type<assign> assign
%type<expr> expr
%type<op> op
%type<block> block
%type<stmts> stmts
%type<ifNode> ifstmt
%type<print> printstmt
%type<whilestmt> whilestmt
%type<program> start
%type<dowhile> dowhile
%type<decl> declstmt
%type<cmpstmt> cmpstmt
%type<arracs> arracs
%type<arrass> arrass
%type<getAddrOp> getaddr
%type<getValOp> getval
%type<callStmt> callStmt
%type<funcDef> funcDef
%type<retStmt> retStmt
%type<paraList> paras
%type<arguList> argus
/* Operator precedence for mathematical operators */
%left ADD SUBS
%left MUL DIV

/* Entry point of grammar */
%start start

%%

start:  
{$$ = new AST::Program();}
     /* empty */ 
| stmts  {
  $$ = new AST::Program($1);
  $$->exec();
  }
;

stmts: stmts SEMI stmt {$1->push_back($3);}
| stmt  {
  $$ = new std::vector<AST::StatementNode*>();
  $$->push_back($1);
}
;

block: LB RB {$$ = new AST::blockNode();}
| LB stmts RB  {$$ = new AST::blockNode($2);}
;


stmt: expr 
| block
| ifstmt
| printstmt
| whilestmt
| dowhile
| declstmt
| retStmt
| funcDef
;

dowhile:
DO block WHILE LP expr RP {
  $$ = new AST::dowhileStatementNode($5, $2);
}
;

whilestmt:
WHILE LP expr RP block {
  $$ = new AST::whileStatementNode($3, $5);
}
;


printstmt: PRINT LP expr RP {
  $$ = new AST::printStatementNode($3);

}
;
ifstmt:  IF LP expr RP block {$$ = new AST::ifStatementNode($3, $5);}
| IF LP expr RP block ELSE block  {$$ = new AST::ifStatementNode($3, $5, $7);}
;

expr: {$$ = new AST::integerNode(0);} 
|IDENTIFY  
|INTEGER  
|assign
|expr op expr {
  $$ = new AST::binOpNode($2, $1, $3);
  }
| LP expr RP {$$ = $2;}
| cmpstmt
| arrass
| arracs
| getaddr
| getval
| callStmt
;

assign: IDENTIFY ASSIGN INTEGER {
  $$ = new AST::assignNode($1, $3);
  }
| IDENTIFY ASSIGN expr {
  $$ = new AST::assignNode($1, $3);
}
;

paras: {$$ = new AST::paraList();}
| paras COMMA TYPE IDENTIFY {
  $1->add($3, $4);
}
| TYPE IDENTIFY {
  $$ = new AST::paraList();
  $$->add($1, $2);
}
;

argus: {$$ = new AST::argumentList();}
| argus COMMA expr {
  $1->add($3); }
| expr {
  $$ = new AST::argumentList();
  $$->add($1);
}
;

funcDef: FUNC IDENTIFY LP paras RP block {
  $$ = new AST::functionDef($2, $4, $6);
};

callStmt: IDENTIFY LP argus RP {
  $$ = new AST::callStmt($1, $3);
};

retStmt: RETURN expr {
  $$ = new AST::returnStmt($2);
};

arracs: IDENTIFY LF expr RF {
  $$ = new AST::arrayAccessExprNode($1, $3);}
| LF expr RF {$$ = new AST::arrayAccessExprNode(nullptr, $2);};

arrass: IDENTIFY LF expr RF ASSIGN expr {
  $$ = new AST::arrayAssignNode ($1, $3, $6);
};

getaddr: AD IDENTIFY {
  $$ = new AST::getAddressOp($2);
};

getval: SP IDENTIFY {
  $$ = new AST::getValueOp($2);
}

declstmt: TYPE IDENTIFY ASSIGN expr {
  $$ = new AST::declStatementNode($1, $2, $4);
}
| TYPE IDENTIFY {
  $$ = new AST::declStatementNode($1, $2, nullptr);
};

cmpstmt: expr CMP expr {
  $$ = new AST::cmpExprNode($2, $1, $3);
}

op: ADD
| SUBS
| MUL
| DIV
| MOD
;

%%

namespace parse
{
    void Parser::error(const location& l, const std::string& m)
    {
        std::cerr << l << ": " << m << std::endl;
        driver.error_ = (driver.error_ == 127 ? 127 : driver.error_ + 1);
    }
}
