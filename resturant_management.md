Table user {
  id int [pk, increment]
  name varchar [not null]
  email varchar [not null, unique]
  phone varchar [unique]
 

  created_at timestamp [not null]
  modified_at timestamp
  deleted_at timestamp
}

Table role {
  id int [pk, increment]
  role_name varchar [not null, unique]

  created_at timestamp [not null]
  modified_at timestamp
  deleted_at timestamp
}

Table user_role_mapped{
  user_id int [not null, ref: > user.id]
  role_id int [not null, ref: > role.id]

   indexes {
    (user_id, role_id) [pk]
  }
}

Table restaurants {
  id int [pk, increment]

  owner_id int [not null, ref: > user.id]

  name varchar [not null]

  street varchar
  city varchar
  state varchar
  country varchar
  zipcode varchar

  created_at timestamp [not null]
  modified_at timestamp
  deleted_at timestamp
}

Table address {
  id int [pk, increment]

  label varchar          // Home, Office, PG, etc.
  street varchar [not null]
  city varchar [not null]
  state varchar [not null]
  country varchar [not null]
  zipcode varchar [not null]

  created_at timestamp [not null]
}

Table user_address_mapped {
  user_id int [not null, ref: > user.id]
  address_id int [not null, ref: > address.id]

  indexes {
    (user_id, address_id) [pk]
  }
}

Table food_items {
  id int [pk, increment]

  restaurant_id int [not null, ref: > restaurants.id]

  food_item_name varchar [not null]

  current_price numeric(10,2) [not null]

  is_available boolean [not null, default: true]

  created_at timestamp [not null]
  modified_at timestamp
  deleted_at timestamp
}

Table food_item_price_history {
  id int [pk, increment]

  food_item_id int [not null, ref: > food_items.id]

  price numeric(10,2) [not null]

  effective_from timestamp [not null]
}

Table orders {
  id int [pk, increment]

  customer_id int [not null, ref: > user.id]

  restaurant_id int [not null, ref: > restaurants.id]

  address_id int [not null, ref: > address.id]

  order_date timestamp [not null]

  status varchar [not null] // Pending, Preparing, Out for Delivery, Delivered, Cancelled

  total_amount numeric(10,2) [not null]

  created_at timestamp [not null]
}

Table order_items {
  id int [pk, increment]

  order_id int [not null, ref: > orders.id]

  food_item_id int [not null, ref: > food_items.id]

  quantity int [not null]

  unit_price numeric(10,2) [not null]

  subtotal numeric(10,2) [not null]
}

Table customer_rating {
  id int [pk, increment]

  order_id int [not null, unique, ref: > orders.id]

  customer_id int [not null, ref: > user.id]

  restaurant_id int [not null, ref: > restaurants.id]

  rating int [not null]

  review text

  created_at timestamp
}

