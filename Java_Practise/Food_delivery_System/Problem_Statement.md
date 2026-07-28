# Online Food Ordering System

Like Swiggy.

**Entities**

Restaurant
MenuItem
Customer
Order
Cart
Coupon

**Requirements**

*Restaurant*

name
rating
isOpen
menu

*Menu Item*

id
name
price
isAvailable
category

*Customer*

id
name
walletBalance

*Cart*

Should

addItem()
removeItem()
changeQuantity()
getTotal()

*Coupon*

Different coupon types

FlatCoupon
PercentageCoupon
BuyOneGetOneCoupon

Every coupon calculates discount differently.

**Hint**:

Coupon (interface) calculateDiscount(...)

*Order States*

CREATED
CONFIRMED
PREPARING
OUT_FOR_DELIVERY
DELIVERED
CANCELLED

**Requirements**

- Customer cannot order unavailable item Restaurant closed cannot order

- Wallet deduct after successful payment

- If payment fails rollback order

**Concepts**
Strategy Pattern
Composition
Enum
Exception Handling
