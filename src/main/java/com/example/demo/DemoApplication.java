package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired private FooRepository fooRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		Stream.of(new Foo(null, "hello"), new Foo(null, "world"))
		.forEach(f -> fooRepo.save(f));

		System.out.println("fooRepo.findFooByName() = " + fooRepo.findFooByName("world"));
		System.out.println("fooRepo.findByName() = " + fooRepo.findByName("world"));
		System.out.println("fooRepo.findByName2() = " + fooRepo.findByName2("world"));
		System.out.println("fooRepo.someCustomFind() = " + fooRepo.someCustomFind("world"));
	}
}


@Entity
@Data
@Table(name = "foo")
@NoArgsConstructor
@AllArgsConstructor
class Foo {
	@Id
	@GeneratedValue
	private String id;
	private String name;
}

interface FooRepository extends CrudRepository<Foo, Long>, CustomFooRepository {
	Foo findFooByName(String name);
	Foo findByName(String name);
	@Query("from Foo where name = :name")
	Foo findByName2(@Param("name") String name);
}

interface CustomFooRepository {
	String someCustomFind(String name);
}

class CustomFooRepositoryImpl implements CustomFooRepository {
	@Autowired private FooRepository repo;

	@Override
	public String someCustomFind(String name) {
		Foo foo = repo.findByName2(name);
		System.out.println("foo = " + foo);
		return "hello";
	}
}
