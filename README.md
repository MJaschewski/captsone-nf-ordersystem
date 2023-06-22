# captsone-nf-ordersystem
Welcome the capstone project. This program can be used as a web application for a order system in a company.
## Define variable:
In backend/src/main/resources/application.properties the environment variable MONGO_DB_CONNECTION_STRING needs to be defined. Set it as a connection string to a mongoDB of your choice.
## Login needs a User
Once you have established the connection to your MongoDB. Open your MongoDB manually with for example MongoCompas. Create a Database named CapstoneOrderSystemDB and a Collection UserList. Now import the UserList.json. Afterwards you can use 3 test Accounts with different authorities. Each user has the password '1234'.
## Product List in Hub (Only with user testPurchase):
You need to add products to your mongodb before you can see any products. This can be done with the add product feature on the website.
## Order List in Hub:
After you added products to your product list you can add orders. Afterwards they are displayed in the order hub.
## Approve orders (Only with users testLead & testPurchase):
If you have authority lead or purchase you can approve orders. You can only approve already existing orders. When both a lead and a purchase have approved the order it's status will change to approved.
