package sm.central;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sm.central.repository.classes.ClassRepository;
import sm.central.model.classes.Class;

@SpringBootApplication
public class SchoolProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolProjectApplication.class, args);
	}
	@Bean
    CommandLineRunner initDatabase(ClassRepository classRepository) {
        return args -> {
            // List of classes from Nursery to 10th
            String[] classNames = {
                "Nursery", "LKG", "UKG", "1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th"
            };

            // Check if classes already exist to avoid duplicates
            if (classRepository.count() == 0) {
                for (String className : classNames) {
                    Class newClass = new Class();
                    newClass.setName(className);
                    newClass.setDescription("Class " + className + " description");
                    classRepository.save(newClass);
                }
                System.out.println("Classes from Nursery to 10th initialized.");
            }
        };
    }

}
