package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.GradeValidator;
import validation.HomeworkValidator;
import validation.StudentValidator;
import validation.Validator;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private static Service service;

    @BeforeEach
    void setUp() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "students.xml");
        HomeworkXMLRepository fileRepository2 = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "grades.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @Test
    void findAllStudents_shouldReturnList() {
        Integer size = 0;
        Iterable<Student> allStudents = service.findAllStudents();
        assertNotNull(allStudents);
        if (allStudents instanceof Collection) {
            size = ((Collection<?>) allStudents).size();
        }
        assertEquals(2, size);
    }

    @Test
    void saveStudent_shouldCreateNewStudent() {
        service.saveStudent("3","Hunor",523);
        Iterable<Student> allStudents = service.findAllStudents();
        AtomicBoolean newStudent = new AtomicBoolean(false);
        allStudents.forEach(student -> {
            System.out.println(student.getID());
            if(student.getID().equals("3")) {
                newStudent.set(true);
            }
        });
        assertTrue(newStudent.compareAndSet(true, true));
    }

    @Test
    void saveGrade_shouldCreateNewGrade() {
        service.saveGrade("3","3", 10.0, 12, "done");
        Iterable<Grade> allGrades = service.findAllGrades();
        AtomicBoolean newGrade = new AtomicBoolean(false);
        allGrades.forEach(grade -> {
            if(grade.getID().getObject2().equals("3")) {
                newGrade.set(true);
            }
        });
        assertEquals(newGrade.get(), true);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "12", "852", "asd"})
    void deleteStudent_shouldDecNumberOfStudents(String id) {
        service.saveStudent("3","Hunor",523);

        int sizeBefore = 0;
        Iterable<Student> allStudents = service.findAllStudents();
        assertNotNull(allStudents);
        if (allStudents instanceof Collection) {
            sizeBefore = ((Collection<?>) allStudents).size();
        }

        service.deleteStudent(id);

        int sizeAfter = 0;
        Iterable<Student> allStudentsAfter = service.findAllStudents();
        assertNotNull(allStudents);
        if (allStudentsAfter instanceof Collection) {
            sizeAfter = ((Collection<?>) allStudentsAfter).size();
        }

        assertNotEquals(sizeBefore-1, sizeAfter);

    }

    @Test
    void updateStudent_shouldUpdateStudent() {
        service.updateStudent("3", "Katona Hunor", 533);
        Iterable<Student> allStudents = service.findAllStudents();
        AtomicBoolean updated = new AtomicBoolean(false);
        allStudents.forEach(student -> {
            if(student.getID().equals("3") && student.getName().equals("Katona Hunor") && student.getGroup() == 533) {
                updated.set(true);
            }
        });
        assertTrue(updated.get());
    }

    @AfterAll
    static void deleteStudentId3(){
        service.deleteStudent("3");
    }

}