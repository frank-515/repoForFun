#include <vector>
#include <string>
#include "../include/configor/json.hpp"
#include <fstream>
using studentName_t = std::string;
using phoneNumber_t = std::string;
using className_t = std::string;
using namespace configor;

class Student{
public:
    studentName_t studentName;
    phoneNumber_t phoneNumber;
    className_t className;
    std::vector<std::string> tags;
    JSON_BIND(Student, studentName, phoneNumber, className, tags);

    Student(studentName_t studentName_, phoneNumber_t phoneNumber_, className_t className_) 
    : studentName(studentName_), phoneNumber(phoneNumber_), className(className_) {}
    Student(studentName_t studentName_) : studentName(studentName_), phoneNumber("nil"), className("nil") {}
    void setTag(std::vector<std::string> tags_) {
        tags = tags_;
    }
    void addTag(std::string tag){
        tags.push_back(tag);
    }
    void removeTag(std::string tag){
        if (tags.size() == 0) return;
        for (auto it = tags.begin(); it != tags.end(); ++it){
            if (*it == tag) tags.erase(it);
            return;
        }
    }
    
};

class studentsList{
public:
    std::string listName;
    std::vector<Student> students;
    
    studentsList() {};
    studentsList(std::string listName){
        setName(listName);
    }
    //studentsList(const json& json_in) {}
    ~studentsList() {}
    void setName(const std::string& name){
        listName = name;
    }
    void clear() { students.clear(); }
    void add(const Student& s){
        students.push_back(s);
    }
    int find_sn(const studentName_t &studentName){
        for (auto it = students.begin(); it != students.end(); it++){
            if (it->studentName == studentName) return it - students.begin();
        }
        return -1;
    }
    int find_pn(const phoneNumber_t &phoneNumber){
        for (auto it = students.begin(); it != students.end(); it++){
            if (it->phoneNumber == phoneNumber) return it - students.begin();
        }
        return -1;
    }
    bool remove_i(int index){
        if (index >= students.size() || index < 0) return false;
        students.erase(students.begin() + index);
        return true;
    }
    bool remove_s(const Student& s){
        int index = find_sn(s.studentName);
        if (index >= students.size() || index < 0) return false;
        students.erase(students.begin() + index);
    }
    bool remove_sn(const studentName_t &studentName){
        int index = find_sn(studentName);
        if (index >= students.size() || index < 0) return false;
        students.erase(students.begin() + index);
        return true;
    }
    bool remove_pn(const phoneNumber_t &phoneNumber){
        int index = find_pn(phoneNumber);
        if (index >= students.size() || index < 0) return false;
        students.erase(students.begin() + index);
        return true;
    }
    bool modify_pn(int index, const phoneNumber_t &phoneNumber){
        if (index >= students.size() || index < 0) return false;
        students.at(index).phoneNumber = phoneNumber;
        return true;
    }
    bool modify_sn(int index, const studentName_t &studentName){
        if (index >= students.size() || index < 0) return false;
        students.at(index).studentName = studentName;
        return true;
    }
    bool modify_cn(int index, const className_t &className){
        if (index >= students.size() || index < 0) return false;
        students.at(index).className = className;
        return true;
    }
    json returnJson(){
        json j;
        for (auto it = students.begin(); it != students.end(); it++){
            j.push_back(*it);
        }
        return j;
    }
    void saveJson(){
        if (listName.empty()) return;
        std::ofstream ofs((listName + ".json").c_str());
        auto j = returnJson();
        ofs << std::setw(4) << j << std::endl;
    }
    void loadJson(){
        std::ifstream ifs((listName + ".json").c_str());
        json j;
        ifs >> j;
        students.clear();
        for (auto & student_j : j){
            students.push_back({student_j["studentName"], student_j["phoneNumber"], student_j["className"]});
            students.back().setTag(student_j["tags"]);
        }
    }
};
