CREATE TABLE developers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(250),
    birth_date DATE,
    sex ENUM('male', 'female', 'unknown')
        NOT NULL,
    email VARCHAR(250),
    skype VARCHAR(250)
   );
CREATE TABLE skills (
    id INT PRIMARY KEY AUTO_INCREMENT,
    industry ENUM('Java', 'C++', 'C#', 'JS')
        NOT NULL,
    skill_level ENUM('Junior', 'Middle', 'Senior')
        NOT NULL
    );
CREATE TABLE companies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200),
    description VARCHAR(150)
);
CREATE TABLE customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200),
    EDRPOU INT,
    product VARCHAR(150)
);
CREATE TABLE project (
    id INT PRIMARY KEY AUTO_INCREMENT,
    start_date date not null,
    name VARCHAR(200),
    description VARCHAR(150),
    company_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL
);
CREATE TABLE projects_developers (
    project_id INT NOT NULL,
    developer_id INT NOT NULL,
    FOREIGN KEY(project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY(developer_id) REFERENCES developers(id) ON DELETE CASCADE
);

CREATE TABLE developers_skills (
    developer_id INT NOT NULL,
    skill_id INT NOT NULL,
    FOREIGN KEY(developer_id) REFERENCES developers(id) ON DELETE CASCADE,
    FOREIGN KEY(skill_id) REFERENCES skills(id) ON DELETE CASCADE
);