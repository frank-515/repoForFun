//
// Created by frank515 on 22-11-16.
//
#include <string>
#include <unordered_map>
#include <vector>
#include <stdexcept>
#include <any>
#include "memory.hpp"

typedef size_t ptr_t;
using std::vector;
using std::string;
using std::unordered_map;
extern const ptr_t STK_SIZE;
extern const ptr_t RZV_MEM_LEN;
#define PRINT(A) std::cout << A << std::endl
#define TOI(A) std::any_cast<int>(A)


enum class SymbolType {
    variable,
    function,
    unknown
};


class Symbol {
public:
    SymbolType type;
    string name;
    std::any val;
    Symbol(SymbolType type, string name, std::any val) : type(type), name(name), val(val) {};
    Symbol() : type(SymbolType::unknown) {};
};

const static Symbol nil(SymbolType::unknown, "", 0);

class SymbolTable {
private:
    unordered_map<string, Symbol> symbolMap;
public:
    Symbol& get(const string name) {
        return symbolMap[name];
    }
    void put(Symbol s) {
        if (this->has(s.name)) throw std::logic_error("Duplicate symbol.");
        symbolMap[s.name] = s;
    }
    bool has(string name) {
        return !symbolMap[name].name.empty();
    }
    SymbolTable() {
        this->symbolMap["_"] = Symbol(SymbolType::variable, "_", ptr_t(0));
    }
};
// Sorry this is a typo QAQ
class Enviroment {
private:
    struct EnvList {
        EnvList *upper;
        SymbolTable *symbolTable;
        ptr_t stackTop;
    }*current {};
    int layer;
public:

    Enviroment() {
        this->layer = 0;
        current = new EnvList;
        current->upper = nullptr;
        current->symbolTable = new SymbolTable();
        current->stackTop = RZV_MEM_LEN;
    }


    Symbol & get(const string name) const {
        auto i = current;
        while (i) {
            if (i->symbolTable->has(name)) return i->symbolTable->get(name);
            i = i->upper;
        }
        return const_cast<Symbol &>(nil);
    }
    void put(const Symbol symbol) const  {
        //PRINT("Symbol putted - " + s.name + " := "+ std::to_string(TOI(s.val)));
        get(symbol.name).val = symbol.val;

    }

    SymbolTable* getCurrTable() {
        return this->current->symbolTable;
    }

    bool currHas(const string name) {
        return current->symbolTable->has(name);

    }

    Symbol& getCurr(const Symbol s) {
        return current->symbolTable->get(s.name);
    }

    void putCurr(const Symbol s) {
        if (currHas(s.name)) {
            throw std::logic_error("Error: Duplicate symbol.");
        }
        current->symbolTable->put(s);
    }

    void push() {
        auto t = current;
        current = new EnvList();
        current->upper = t;
        current->symbolTable = new SymbolTable();
        current->stackTop = t->stackTop;
        layer++;
    }

    void pop() {
        auto t = current->upper;
        delete current->symbolTable;
        delete current;
        current = t;
        layer--;
    }

    bool has(const string name) {
        return !this->get(name).name.empty();
    }

    int getLayer() {
        return this->layer;
    }

    ptr_t& getStack()  {
        return current->stackTop;
    }
};
