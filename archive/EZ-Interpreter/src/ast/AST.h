//
// Created by frank515 on 22-11-30.
//

#ifndef PARSER_AST_H
#define PARSER_AST_H

#endif //PARSER_AST_H
#define GT std::cerr << __LINE__ << std::endl;
#include <string>
#include <iostream>
#include <any>
#include <utility>
#include "symbolTable.hpp"
extern const size_t RETURN_VAL;
using std::any;
using std::string;

extern Enviroment* env;
extern Memory* mem;

namespace AST {
    enum class cmp {
        EQ,
        LS,
        LE,
        NE,
        BS,
        BE
    };

    enum class basicType {
        integer,
        character
    };

    enum class binOP {
        ADD,
        SUBS,
        MUL,
        DIV,
        MOD
    };
    class Node {

    };

    class StatementNode : public Node {
    public:
        virtual void run() const {};
    };

    class blockNode : public StatementNode {
    private:
        vector<StatementNode*> *stmts;
    public:
        bool *returned = new bool(false);
        any *returnVal;
        void run() const override;

        explicit blockNode(vector<StatementNode*> *stmts)
            : stmts(stmts), returnVal(new any(0)) {};
        blockNode() : returnVal(new any(0)) {};
        void push_stmt(StatementNode* stmt);
    };
    class ExprNode : public StatementNode{
    public:
        virtual any eval() const = 0;

        void run() const override;
    };

    class printStatementNode : public StatementNode {
    private:
        ExprNode *exprNode;
    public:
        explicit printStatementNode(ExprNode* exprNode) : exprNode(exprNode) {
            // For compile-time print
            // std::cout << std::any_cast<int>(exprNode->eval()) << std::endl;
        };
        void run() const override ;
    };

    class integerNode : public ExprNode{
    private:
        int val = 0;
    public:
        explicit integerNode(int val) : val(val) {};
        int getValue() const {return val;}
        any eval() const override {return val;}
    };

    class identifyNode : public ExprNode{
    private:
        string name;
    public:
        explicit identifyNode(string name) : name(name) {};

        string getName() {return name;}

        any eval() const override;
    };

    class arrayAccessExprNode : public ExprNode {
    protected:
        identifyNode *id;
        ExprNode *offset;
    public:
        arrayAccessExprNode(identifyNode *id, ExprNode *offset)
                : id(id), offset(offset) {};
        any eval() const override;
    };

    class binOpNode : public ExprNode {
    private:
        binOP op;
        ExprNode *LHS, *RHS;
    public:
        binOpNode(binOP op, ExprNode *LHS, ExprNode *RHS) : op(op), LHS(LHS), RHS(RHS) {};
        any eval() const override;
    };

    class assignNode : public ExprNode {
    private:
        identifyNode *id;
        ExprNode *val;
    public:
        assignNode(identifyNode *id, ExprNode *val) : id(id), val(val) {};
        any eval() const override ;

    };

    // As same as '&' on c
    class getAddressOp : public ExprNode {
    private:
        identifyNode *id;
    public:
        getAddressOp(identifyNode *id) : id(id) {};

        any eval() const override;
    };

    // As same as '*' on c, '#' on mine lang
    class getValueOp : public ExprNode {
    private:
        identifyNode *id;
    public:
        getValueOp(identifyNode *id) : id(id) {};
        any eval() const override ;
    };

    class declStatementNode : public StatementNode {
    private:
        basicType type;
        identifyNode *id;
        ExprNode *val;
    public:
        declStatementNode(basicType type, identifyNode *id, ExprNode *val)
                : type(type), id(id), val(val) {};
        void run() const override;
    };

    class arrayAssignNode : public ExprNode {
    private:
        ExprNode *val;
        ExprNode *offset;
        identifyNode *id;
    public:
        arrayAssignNode(identifyNode *id, ExprNode *offset, ExprNode *val)
                :  val(val), offset(offset), id(id) {};

        any eval() const override;
    };

    class ifStatementNode : public StatementNode {
    private:
        blockNode *ifBlock;
        blockNode *elseBlock;
        ExprNode *expr;
    public:
        bool *returned = new bool(false);
        ifStatementNode(ExprNode* expr, blockNode *ifBlock, blockNode *elseBlock) :
                ifBlock(ifBlock), elseBlock(elseBlock), expr(expr) {};
        ifStatementNode(ExprNode* expr, blockNode *ifBlock) :
                ifBlock(ifBlock), elseBlock(new blockNode()),expr(expr) {};

        void run() const override ;
    };

    class dowhileStatementNode : public StatementNode {
    private:
        ExprNode *expr;
        blockNode *block;
    public:
        bool *returned = new bool(false);
        dowhileStatementNode(ExprNode *exprNode, blockNode *blockNode)
                : expr(exprNode), block(blockNode) {};
        void run() const override;
    };

    class whileStatementNode : public StatementNode {
    private:
        ExprNode *expr;
        blockNode *block;
    public:
        bool *returned = new bool(false);
        whileStatementNode(ExprNode *expr, blockNode *block)
                : expr(expr), block(block) {};
        void run() const override;

    };

    class cmpExprNode : public ExprNode {
    private:
        cmp cmpType;
        ExprNode *LHS, *RHS;
    public:
        cmpExprNode(cmp cmpType, ExprNode *LHS, ExprNode *RHS)
                : cmpType(cmpType), LHS(LHS), RHS(RHS) {};
        static int any2Int(any const& i) {
            if (i.type() == typeid(int)) return std::any_cast<int>(i);
            if (i.type() == typeid(bool)) return static_cast<int>(std::any_cast<bool>(i));
            if (i.type() == typeid(float)) return static_cast<int>(std::any_cast<float>(i));
            if (i.type() == typeid(double)) return static_cast<int>(std::any_cast<double>(i));
            if (i.type() == typeid(char)) return static_cast<int>(std::any_cast<char>(i));
            else throw std::logic_error("Error: unable to cast to int on Any2Int().");
        }
        any eval() const override;
    };


    class paraList : public Node {
    public:
        paraList() = default;

        std::vector<std::pair<basicType, identifyNode*>> list;
        void add(basicType type, identifyNode* id) {
            list.emplace_back(type, id);
        }
    };

    //This is not a statement but... hard to change the code...
    //So I designed this node that run to add pointer to environment;

    struct Function{
        paraList *paras;
        blockNode *block;
        Function(paraList *list, blockNode *block)
                : paras(list), block(block) {};
    };

    class functionDef : public StatementNode {
    private:
        identifyNode *id;
        paraList *list;
        blockNode *block;
    public:
        Function *func;
        functionDef(identifyNode *id, paraList *list, blockNode* block)
                : id(id), list(list), block(block) {
            func = new Function(list, block);
        };

        void run() const override ;
        void initStack();
    };

    class argumentList : public Node {
    public:
        vector<ExprNode*> arguments;
        void add(ExprNode* val) {
            arguments.push_back(val);
        }
    };

    class callStmt : public ExprNode {
    private:
        identifyNode *id;
        argumentList *list;
    public:
        callStmt(identifyNode *id, argumentList *list)
                : id(id), list(list) {};

        any eval() const override;
    };

    class returnStmt : public StatementNode {
    private:
        ExprNode *val;
    public:
        any *returnVal;
        returnStmt(ExprNode *val) : val(val), returnVal(new any) {};

        void run() const override ;
    };


    class Program {
    private:
        AST::blockNode *rootBlock;
    public:
        Program(AST::blockNode *rootBlock) : rootBlock(rootBlock) {};
        Program() : rootBlock(new blockNode()) {};
        Program(std::vector<AST::StatementNode*>* stmts) {
            rootBlock = new blockNode(stmts);
        }

        void exec() ;
    };


}