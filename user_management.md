Table User {
  id int [pk]
  name varchar [not null]
  email varchar [not null, unique]
  phone_number varchar
  age int
  gender bool

  joining_date date

  created_at timestamp
  modified_at timestamp
  deleted_at timestamp
}

Table Role {
  id int [pk]
  name varchar [not null, unique]
}

Table User_Role_Mapped {
  user_id int
  role_id int

  indexes {
    (user_id, role_id) [pk]
  }
}

Table Hospital_Network {
  id int [pk]
  name varchar [not null]

  created_at timestamp
  modified_at timestamp
}

Table Branch {
  id int [pk]

  hospital_network_id int

  name varchar
  location varchar

  created_at timestamp
}

Table Department {
  id int [pk]

  branch_id int

  name varchar

  created_at timestamp
}

Table User_Hospital_Network_Mapped {
  user_id int
  hospital_network_id int

  indexes {
    (user_id, hospital_network_id) [pk]
  }
}

Table User_Branch_Mapped {
  user_id int
  branch_id int

  indexes {
    (user_id, branch_id) [pk]
  }
}

Table User_Department_Mapped {
  user_id int
  department_id int

  indexes {
    (user_id, department_id) [pk]
  }
}

Ref: User_Role_Mapped.user_id > User.id
Ref: User_Role_Mapped.role_id > Role.id

Ref: Branch.hospital_network_id > Hospital_Network.id
Ref: Department.branch_id > Branch.id

Ref: User_Hospital_Network_Mapped.user_id > User.id
Ref: User_Hospital_Network_Mapped.hospital_network_id > Hospital_Network.id

Ref: User_Branch_Mapped.user_id > User.id
Ref: User_Branch_Mapped.branch_id > Branch.id

Ref: User_Department_Mapped.user_id > User.id
Ref: User_Department_Mapped.department_id > Department.id
