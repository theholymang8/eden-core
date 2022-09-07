# BSc Dissertation Project/Backend Unit - Eden Core

Complete Code Repository for the backend unit of Eden Architecture. Built in Spring Boot. Feel Free to fork it.

This repository the RESTful API Unit which communicates with the rest of the architecture modules.  

Features include but not limited to:

- [Authentication]  
- [User Profiles]   
- [User Roles]   
- [API]  
- [DB Creation]
- [Reommendation Services] 


<div align="center">

<sub>Built with ❤︎ by Ioannis Anastasopoulos</sub>
</div>

</br>


## Details

###Dependencies

All application dependencies are managed by Maven. The whole project lifecycle is managed by Maven.

###Database Generation

The application is run with the use of profiles. In addition to profiles, the database schema can be generated once connected to a local/remote database with a configuration setting to **create** in the **hibernate.ddl.auto** option.

###Profiles

In order to generate sample data, certain profiles can be used. For post generation, the application should run with the following active profile

 - post-dataseed

Whilst for user generation, the following profile should be active

 - generate-users


## Contact
Ioannis Anastasopoulos - giannisanast34@gmail.com-Application-Template](https://github.com/Spring-Boot-Framework/Spring-Boot-Application-Template)
