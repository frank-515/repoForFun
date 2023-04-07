//
// Created by frank515 on 22-11-16.
//

#include "AST.h"
#ifndef BISON_FLEX_CPP_TEMPLATE_ASTNODE_H
#define BISON_FLEX_CPP_TEMPLATE_ASTNODE_H



namespace AST {

    void setReturnVal(int i) {
        mem->at(RETURN_VAL);
        mem->setInt(i);
    }

    int getReturnVal() {
        auto val = mem->getInt(RETURN_VAL);
        setReturnVal(0);
        return val;
    }

    void blockNode::run() const {
        *returned = false;
        if (!stmts || stmts->empty()) return;
        env->push();
        for (StatementNode* stmt : *stmts) {

            stmt->run();
            if (auto retStmt = dynamic_cast<returnStmt*>(stmt)) {
                //*returnVal = *retStmt->returnVal;
                *returned = true;
                break;
            }
            if (auto ifStmt = dynamic_cast<ifStatementNode*>(stmt)) {
                *returned = *ifStmt->returned;
                *ifStmt->returned = false;
            }
            if (auto dowhileStmt = dynamic_cast<dowhileStatementNode*>(stmt)) {
                *returned = *dowhileStmt->returned;
                *dowhileStmt->returned = false;
            }
            if (auto whileStmt = dynamic_cast<whileStatementNode*>(stmt)) {
                *returned = *whileStmt->returned;
                *whileStmt->returned = false;
            }
            if (*returned) {
                break;
            }

        }
        env->pop();
    }

    void blockNode::push_stmt(AST::StatementNode *stmt) {
        stmts->push_back(stmt);
    }

    void ExprNode::run() const { eval(); }


    void printStatementNode::run() const {
        // Now just support int type to print
        std::cout << std::any_cast<int>(exprNode->eval()) << std::endl;
    }

    any identifyNode::eval() const {
        if (env->has(name)) {
            return mem->getInt(std::any_cast<ptr_t>(env->get(name).val));
            // return env->get(name).val;
        }
        throw std::logic_error("Error: Undefined symbol " + this->name);
    }

    any arrayAccessExprNode::eval() const {

            int offsetI = std::any_cast<int>(offset->eval());
            if (id && env->get(id->getName()).type != SymbolType::variable)
                throw std::logic_error("Error: Can't access a non-variable symbol by address operation.");
            ptr_t base = id ? std::any_cast<ptr_t>(env->get(id->getName()).val) : 0;
            if (!id) return mem->getInt(offsetI);
            return mem->getInt(base + sizeof(int) / sizeof(char) * offsetI);
    }


    any binOpNode::eval() const {
        int LHV = std::any_cast<int>(LHS->eval());
        int RHV = std::any_cast<int>(RHS->eval());
        int val = 0;
        switch (op) {
            case binOP::ADD:
                val = LHV + RHV;
                break;
            case binOP::SUBS:
                val = LHV - RHV;
                break;
            case binOP::MUL:
                val = LHV * RHV;
                break;
            case binOP::DIV:
                val = LHV / RHV;
                break;
            case binOP::MOD:
                val = LHV % RHV;
        }
        return static_cast<any>(val);
    }


    any assignNode::eval() const {
        if (env->has(id->getName())) {
            ptr_t ptr = std::any_cast<ptr_t>(env->get(id->getName()).val);
            // Just to change the value which the val-pointer pointed to.
            mem->at(ptr);
            if (val->eval().type() == typeid(int)) {
                mem->setInt(std::any_cast<int>(val->eval()));
            } else if (val->eval().type() == typeid(char)) {
                mem->setChar(std::any_cast<char>(val->eval()));
            }

            // env->put(Symbol(SymbolType::variable, id->getName(), val->eval()));

        } else {
            throw std::logic_error("Error: Undefined symbol.");
        }
        return mem->getInt(std::any_cast<ptr_t>(env->get(id->getName()).val));
        //return env->get(id->getName()).val;
    }

    any getAddressOp::eval() const {
        if (env->get(id->getName()).type == SymbolType::variable) {
            return (int)std::any_cast<ptr_t>(env->get(id->getName()).val);
        } else throw std::logic_error("Error: can not get address of a non-variable symbol.");
    }

    any getValueOp::eval() const {
        return mem->getInt(
                (ptr_t)mem->getInt(std::any_cast<ptr_t>(env->get(id->getName()).val))
        );
    }

    void declStatementNode::run() const  {
        any eval = env ? val->eval() : 0;
        ptr_t ptr = env->getStack();
        mem->at(ptr);
        // make a new mem on the stack top and assigned according value
        if (type == basicType::integer) {
            mem->setInt(std::any_cast<int>(eval));
            env->getStack() += sizeof(int) / sizeof(unsigned char);
        } else if (type == basicType::character) {
            mem->setChar(std::any_cast<char>(eval));
            env->getStack() += sizeof(char) / sizeof(unsigned char);
        }
        env->putCurr(Symbol(SymbolType::variable, id->getName(), ptr));
    }

    any arrayAssignNode::eval() const {
        int valI = std::any_cast<int>(val->eval());
        int offsetI = std::any_cast<int>(this->offset->eval());
        mem->at(std::any_cast<ptr_t>(env->get(id->getName()).val) + offsetI);
        mem->setInt(valI);
        return mem->getInt(std::any_cast<ptr_t>(env->get(id->getName()).val) + offsetI);
    }

    void dowhileStatementNode::run() const  {
        bool doLoop;
        any value;
        loop:
        value = expr->eval();
        if (value.type() == typeid(int)) {
            doLoop = std::any_cast<int>(value) != 0;
        } else if (value.type() == typeid(double)) {
            doLoop = std::any_cast<double>(value) != 0.0;
        } else if (value.type() == typeid(bool)) {
            doLoop = std::any_cast<bool>(value);
        }


        block->run();
        if (doLoop) goto loop;
        *returned = *block->returned;
        *block->returned = false;
    }

    void ifStatementNode::run() const {
        //default for else or null brunch
        bool brunch = false;
        if (expr->eval().type() == typeid(int)) {
            brunch = std::any_cast<int>(expr->eval()) == 0;
        } else if (expr->eval().type() == typeid(double)) {
            brunch = std::any_cast<double>(expr->eval()) == 0.0;
        } else if (expr->eval().type() == typeid(bool)) {
            brunch = std::any_cast<bool>(expr->eval());
        }

        if (!brunch) {
            ifBlock->run();
            *returned = *ifBlock->returned;
            *ifBlock->returned = false;
        } else {
            elseBlock->run();
            *returned = *elseBlock->returned;
            *ifBlock->returned = false;
        }
    }

    void whileStatementNode::run() const  {
        bool doLoop;
        any val;
        loop:
        val = expr->eval();
        if (val.type() == typeid(int)) {
            doLoop = std::any_cast<int>(val) != 0;
        } else if (val.type() == typeid(double)) {
            doLoop = std::any_cast<double>(val) != 0.0;
        } else if (val.type() == typeid(bool)) {
            doLoop = std::any_cast<bool>(val);
        }
        if (!doLoop) goto quit;
        if (*returned) goto quit;
        block->run();
        *returned = *block->returned;

        goto loop;

        quit:;

    }


    any cmpExprNode::eval() const {
        int LHV = any2Int(LHS->eval());
        int RHV = any2Int(RHS->eval());
        bool EQ = LHV == RHV;
        bool LS = LHV < RHV;
        int ret;
        switch (cmpType) {
        case cmp::EQ:
        ret = EQ;break;
        case cmp::LS:
        ret = LS;break;
        case cmp::LE:
        ret = LS && EQ;break;
        case cmp::NE:
        ret = !EQ;break;
        case cmp::BS: // >
        ret = !EQ && !LS;break;
        case cmp::BE: // >=
        ret = !LS && EQ;break;
        }
    return static_cast<int>(ret);
    }

    void functionDef::run() const {
        env->putCurr(Symbol(SymbolType::function, id->getName(), func));
    }

    void functionDef::initStack() {
        for (auto i: list->list) {
            env->put(Symbol(SymbolType::variable, i.second->getName(), 0));
        }
    }

    void Program::exec() {
        try {
            rootBlock->run();
        } catch (std::exception &e) {
            std::cout << e.what() << std::endl;
        }
    }

    any callStmt::eval() const  {
        // To guarantee invoke is legal
        if (!env->has(id->getName()))
            throw std::logic_error("Error: Symbol " + id->getName() + " not in scope.");

        if (env->get(id->getName()).type != SymbolType::function)
            throw std::logic_error("Error: Symbol " + id->getName() + " is not a function.");
        Function *func = std::any_cast<Function*>(env->get(id->getName()).val);
        if (func->paras->list.size() != list->arguments.size())
            throw std::logic_error("Error: Function " + id->getName() +
                                   " needs " + std::to_string(func->paras->list.size()) + " argument(s), But " +
                                   std::to_string(list->arguments.size()) + " given.");


        auto paraCount = func->paras->list.size();

        env->push();
        for (unsigned long i = 0; i < paraCount; i++) {
            auto val = std::any_cast<int>(list->arguments.at(i)->eval());
            ptr_t ptr = env->getStack();
            mem->at(ptr);
            mem->setInt(val);
            env->getStack() += sizeof(int) / sizeof(char);
            Symbol pass(SymbolType::variable, func->paras->list[i].second->getName(), ptr);
            env->putCurr(pass);
        }
        func->block->run();
        env->pop();
        return getReturnVal();
    }

    void returnStmt::run() const {
        //*returnVal = val->eval();
        auto ret = std::any_cast<int>(val->eval());
        setReturnVal(ret);
    }







} // AST

#endif //BISON_FLEX_CPP_TEMPLATE_ASTNODE_H
