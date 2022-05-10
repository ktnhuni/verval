package service;

import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

public class MockitoServiceTest {
        @Mock
        StudentXMLRepository fileRepository1;
        @Mock
        HomeworkXMLRepository fileRepository2;
        @Mock
        GradeXMLRepository fileRepository3;

        static Service service;

        @BeforeEach
        public void setUp() {
                fileRepository1 = mock(StudentXMLRepository.class);
                fileRepository2 = mock(HomeworkXMLRepository.class);
                fileRepository3 = mock(GradeXMLRepository.class);

                Mockito.when(fileRepository1.delete(anyString())).thenReturn(new Student("0","OK", 0));
                Mockito.when(fileRepository2.delete(anyString())).thenReturn(new Homework("0", "", 0, 0));
                Mockito.when(fileRepository2.save(any(Homework.class))).thenReturn(new Homework("0", "", 0, 0));

                service = new Service(fileRepository1, fileRepository2, fileRepository3);
        }

        @Test
        void shouldDeleteStudent() {
                assertNotNull(service.deleteStudent("1"));
                Mockito.verify(fileRepository1).delete("1");
        }

        @Test
        void shouldDeleteHomework() {
                assertNotNull(service.deleteHomework("1"));
        }

        @Test
        void shouldSaveHomework() {
                assertNotNull(service.saveHomework("0","", 0, 0));
        }
}
