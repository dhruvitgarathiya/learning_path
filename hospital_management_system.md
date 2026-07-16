Table Hospital {
  id INT pk
  name VARCHAR 
  Location varchar
}

Table patient {
  id INT Pk
  name VARCHAR
  age int
  gender varchar
  user int
}

ref: user.id < patient.user

Table doctor_hospital_mapped{
  doctor int
  hospital int
}

Table doctors{
  id INT pk
  name varchar
  department_Where_he_is_working varchar
  user_id int
  consultation_charges int
  specilization int
  availability bool
}

ref: user.id < doctors.user_id

Table doctor_patient_mapped{
  doctor int
  patient int
}

Table required_qualification{
  id int
  name varchar
}

Table specilization_which_hospital_offers{
  id int
  name varchar
}

Table qualification{
   doctor int
   degree_qualified int
   passing_year int
}

Table working_shifts_availble_in_hospital{
  id int
  hospital int
  start_time time
  end_time time
}

Table working_shift_table{
  doctor int
  shift int
}

Table consultation{ 
  doctor int
  date_of_consulatation date
  appointment_id int
  dignosis int
  prescription int
  consultation_notes text
  visit_id int
  patient int
  date date
}

Table department{
  id int pk
  name varchar
}

Table diagnosis {
  id int
  doctor_who_is_doing_dignosis integer
  patient_whose_dignosis_is int
  disease varchar
  sevearity varchar
  cause varchar
  date date
  visit int
}

ref: visit.visit_id < diagnosis.visit

Table medicin{
  id int
  name varchar
  batch_number int
  dose int
  price int
}


table prescription{
  id int
  medicin_prescibed int
  dose int
  route varchar
  frequency varchar
  timing varchar
  number_of_refills varchar
  strength int
  form varchar
  patient int
  date date
  visit int
  diagnosis int
}

ref : visit.visit_id < prescription.visit
ref : diagnosis.id < prescription.diagnosis


Table appointment{
  id int
  doctor int
  patient int
  time_of_appointment time
  appointment_status varchar
  date date
}


Table visit{
  visit_id int
  hospital int
  patient int
  date_of_visit date
}

Table laboratory{
  id int
  hospital_where_laboratory_is int
  location int
}

Table lab_technician{
  id int
  employee_id int
  laboratory_In_which_he_is_working int
}

Table Laboratory_test{
  id int
  test_name varchar
  lab_technician int
  consulation_in_which_test_prescribed int
  patient_id int
  result int
}

Table Lab_test_result{
  result_id int
  test_result_file_path varchar
  result_date date
  visit_id int
  patient int
}

Table radiology_service{
  id int
  name int
  charge int
}

Table radiology_test{
  id int
  service int
  result int
  patient_id int
  consultation_in_which_test_prescribed int
  visit int
}

ref : visit.visit_id < radiology_test.visit

Table supplier{
  id int
  name varchar
}

Table hospital_supplier_mapped{
  hospital int
  supplier int
}

Table supplier_medicin_mapped{
  supplier int
  medicin int
}

Table medicin_inventory{
  id int
  medicin int
  quantity int
  is_availble bool
  expiray_date date
}

Table admission{
  hospital int
  patient int
  ward_no int
  room_no int
  bed_no int
  admission_date date
  discharge_date date
  discharge_summery varchar
  doctor int
}


Table wards{
  hospital int
  number int
  location varchar
  charge number
}

Table room {
  hospital int
  ward_no int
  number int
  location varchar

}

Table bed{
  hospital int
  room_no int
  bed_no int
  location varchar
}

ref : wards.(hospital,number) < room.(hospital,ward_no)
ref : room.(hospital,number) < bed.(hospital,room_no)

ref: wards.(hospital, number) < admission.(hospital,ward_no)
ref: room.(hospital, number) < admission.(hospital,room_no)
ref: bed.(hospital,bed_no) < admission.(hospital,bed_no)


Table transfer_request{
  hospital int
  patient_id int
  admission_date date
  current_room_no int
  transfer_room_no int
  request_date date
  request_status int
}

ref: admission.(patient,admission_date) < transfer_request.(patient_id,admission_date)
ref: room.(hospital, number) < transfer_request.(hospital,current_room_no)
ref: room.(hospital, number) < transfer_request.(hospital,transfer_room_no)






Table nurse {
  id int
  user_id int
  department int
}

ref: user.id < nurse.user_id

Table patient_observation{
observation_date date
nurse int
blood_pressure number
oxygen_level number
pulse_rate number
medication_administrated text
daily_progress_notes text
patient int
admission_date date
}


ref: admission.(patient,admission_date) < patient_observation.(patient,admission_date)

ref: patient.id < patient_observation.patient

Table insurance_company{
  id int
  name varchar
}


Table hospital_insurance_company_mapped{
  insurance_company int
  hospital int
}

Table insurance_policy{
  insurance_company int
  policy_name varchar
  policy_id int pk
  maxium_covrage int
}

Table insurance_coverage{
  insurance_company int
  policy_id int
  patient_id int
  covered_amount int
  start_date date
  end_date date
}

ref : insurance_policy.(insurance_company,policy_id) < insurance_coverage.(insurance_company,policy_id)
ref : patient.(id) < insurance_coverage.(patient_id)

Table claim_request{
  policy_id int
  patient_id int
  request_date date
}

ref: insurance_coverage.(policy_id,patient_id) < claim_request.(policy_id,patient_id)

Table claim_detail{
  patient int
  visit int
  admission_date int
  policy_id int
  request_date date
  request_status varchar
  approved_amount number
  rejected_amount number
}

ref : visit.visit_id < claim_detail.visit
ref: claim_request.(policy_id,patient_id) < claim_detail.(policy_id,patient)




Table surgery{
  surgery_id int
  surgery_name varchar
  surgery_type varchar
}

Table doctor_surgery_mapped{
  doctor int
  surgery int
}

Table surgery_admitted_patient_mapped{
  surgery int
  patient int
  admission_date date
  hospital int
  operation_theator varchar
  surgery_team int
  surgey_schedual time
  surgery_status varchar
  surgery_notes text
  surgery_date date
}

ref: admission.(patient,admission_date) < surgery_admitted_patient_mapped.(patient,admission_date)


Table anesthesiologist{
  user_id int
  id int
  name int
}
ref: user.id < anesthesiologist.user_id

Table surgical_team{
  surgical_team_id int
  doctor int
  nurse int
  anesthesiologist int
}


Table doctor_surgical_team_mapped{
  team int
  doctor int
}

Table nurse_surgical_team_mapped{
  team int
  nurse int
}

Table anesthesiologist_surgical_team_mapped{
  team int
  anesthesiologist int
}

Table user{
  id int
  name varchar
  role int
  department int
  contact_number varchar
  email varchar
}

Table role{
  id int
  name varchar
}

ref : role.id < user.role
ref : department.id < user.department


Table document{
  id int
  name varchar
  patient int
  type int
  description text
  url varchar
  uploaded_at date
}

ref : document_types.id < document.type

ref : patient.id < document.patient



Table document_types{
  id int
  name varchar
}

Table medical_history{
  patient_id int
  visit int
  diagonosis int
  prescription int
  allergies int
  chronic_diseas varchar
  surgeries int
}

ref : patient.id < medical_history.patient_id
ref : visit.visit_id < medical_history.visit
ref : diagnosis.id < medical_history.diagonosis
ref: prescription.id < medical_history.prescription
ref : allergies.id   < medical_history.allergies
ref : surgery.surgery_id < medical_history.surgeries
ref : diagnosis.disease < medical_history.chronic_diseas


Table allergies{
  id int
  name varchar
}



Table patient_allergies_mapped{
  patient int
  allergy int
}

ref : allergies.id < patient_allergies_mapped.allergy
ref : patient.id < patient_allergies_mapped.patient



Table bill{
  id int
  patient int
  visit int
  consultation_fees int
  laboratory_charges int
  rediology_charges int
  medicin_charges int
  room_charges int
  surgery_charges int
  nursing_charges int
  other_service_charges int
  discount int
  insurance_claims int
  taxes int
  refunds int
  final_amonut int
}

ref : discout.id < bill.discount
ref : patient.id < bill.patient
ref : visit.visit_id < bill.visit

ref : claim_detail.(visit, patient) < bill.(visit,patient)

ref : bill_tax.(bill, tax) < bill.(id,taxes)

Table bill_tax{
  bill int
  tax int
  taxble_value int
  tax_amount int
}

Table tax {
  id int
  tax_name varchar
  tax_rate int
  tax_number int
  SAC_code int
}

ref : tax.id < bill_tax.tax

Table discout {
  id int
  name varchar
  percentage varchar
  discount_type int
  disocunt_calculation int
  application_point int
}

ref : discount_bill_mapped.bill <  bill.id
ref : discount_bill_mapped.discount < discout.id
ref : discount_application_point.id < discout.application_point
ref : discount_calculation_type.id < discout.disocunt_calculation
ref : Discount_type.id < discout.discount_type

Table discount_bill_mapped{
  bill int
  discount int
}

Table discount_calculation_type{
  id int
  type varchar
}

Table discount_application_point{
  id int
  type varchar
}

Table Discount_type{
  id int
  type varchar
}

Table upfront_deposits{
  id int
  name varchar
  amount int
}

ref : upfront_deposits.id < patient_despoits.upfront_deposit
ref : admission.(patient,admission_date) < patient_despoits.(patient,admission_date)

Table patient_despoits{
  upfront_deposit int
  patient int
  admission_date date
}

Table audit_logs{
  id int
  table_name varchar
  column_name varchar
  old_val varchar
  modified_val varchar
  modification_date date
}

Ref: "consultation"."prescription" < "prescription"."id"

Ref: "prescription"."medicin_prescibed" < "medicin"."id"


Ref: "consultation"."doctor" < "doctors"."id"


Ref: "consultation"."dignosis" < "diagnosis"."id"

Ref: "doctors"."department_Where_he_is_working" < "department"."id"

Ref: "qualification"."degree_qualified" < "required_qualification"."id"

Ref: "doctors"."id" < "qualification"."doctor"

Ref: "doctors"."specilization" < "specilization_which_hospital_offers"."id"

Ref: "doctors"."id" < "working_shift_table"."doctor"

Ref: "working_shift_table"."shift" < "working_shifts_availble_in_hospital"."id"

Ref: "doctor_hospital_mapped"."doctor" < "doctors"."id"

Ref: "doctor_hospital_mapped"."hospital" < "Hospital"."id"

Ref: "doctors"."id" < "appointment"."doctor"

Ref: "patient"."id" < "appointment"."patient"

Ref: "consultation"."appointment_id" < "appointment"."id"

Ref: "visit"."hospital" < "Hospital"."id"

Ref: "visit"."patient" < "patient"."id"

Ref: "doctors"."id" < "diagnosis"."doctor_who_is_doing_dignosis"

Ref: "patient"."id" < "diagnosis"."patient_whose_dignosis_is"

Ref: "lab_technician"."id" < "Laboratory_test"."lab_technician"



Ref: "consultation"."appointment_id" < "Laboratory_test"."consulation_in_which_test_prescribed"

Ref: "Hospital"."id" < "laboratory"."hospital_where_laboratory_is"

Ref: "laboratory"."id" < "lab_technician"."laboratory_In_which_he_is_working"

Ref: "radiology_test"."consultation_in_which_test_prescribed" < "consultation"."appointment_id"

Ref: "radiology_service"."id" < "radiology_test"."service"

Ref: "patient"."id" < "Laboratory_test"."patient_id"

Ref: "patient"."id" < "radiology_test"."patient_id"


Ref: "patient"."id" < "prescription"."patient"

Ref: "supplier"."id" < "supplier_medicin_mapped"."supplier"

Ref: "medicin"."id" < "supplier_medicin_mapped"."medicin"

Ref: "Hospital"."id" < "hospital_supplier_mapped"."hospital"

Ref: "supplier"."id" < "hospital_supplier_mapped"."supplier"

Ref: "medicin_inventory"."medicin" < "medicin"."id"

Ref: "Hospital"."id" < "admission"."hospital"

Ref: "patient"."id" < "admission"."patient"

Ref: "doctors"."id" < "admission"."doctor"

Ref: "radiology_test"."result" < "Lab_test_result"."result_id"

Ref: "Laboratory_test"."result" < "Lab_test_result"."result_id"

Ref: "visit"."visit_id" < "consultation"."visit_id"

Ref: "visit"."visit_id" < "Lab_test_result"."visit_id"

Ref: "Hospital"."id" < "transfer_request"."hospital"

Ref: "patient"."id" < "transfer_request"."patient_id"

Ref: "admission"."admission_date" < "transfer_request"."admission_date"

Ref: "nurse"."id" < "patient_observation"."nurse"

Ref: "insurance_company"."id" < "hospital_insurance_company_mapped"."insurance_company"

Ref: "Hospital"."id" < "hospital_insurance_company_mapped"."hospital"

Ref: "insurance_company"."id" < "insurance_policy"."insurance_company"

Ref: "insurance_company"."id" < "insurance_coverage"."insurance_company"

Ref: "insurance_policy"."policy_id" < "insurance_coverage"."policy_id"

Ref: "insurance_policy"."policy_id" < "claim_request"."policy_id"

Ref: "patient"."id" < "claim_request"."patient_id"

Ref: "surgery"."surgery_id" < "surgery_admitted_patient_mapped"."surgery"

Ref: "surgical_team"."surgical_team_id" < "doctor_surgical_team_mapped"."team"

Ref: "doctors"."id" < "surgical_team"."doctor"

Ref: "doctors"."id" < "doctor_surgery_mapped"."doctor"

Ref: "surgery"."surgery_id" < "doctor_surgery_mapped"."surgery"

Ref: "anesthesiologist"."id" < "surgical_team"."anesthesiologist"

Ref: "nurse"."id" < "surgical_team"."nurse"

Ref: "anesthesiologist"."id" < "anesthesiologist_surgical_team_mapped"."anesthesiologist"

Ref: "surgical_team"."surgical_team_id" < "anesthesiologist_surgical_team_mapped"."team"

Ref: "surgical_team"."surgical_team_id" < "nurse_surgical_team_mapped"."team"

Ref: "nurse"."id" < "nurse_surgical_team_mapped"."nurse"

Ref: "doctors"."id" < "doctor_patient_mapped"."doctor"

Ref: "patient"."id" < "doctor_patient_mapped"."patient"
