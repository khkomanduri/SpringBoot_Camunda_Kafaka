// Import Java's System class for logging
var System = Java.type('java.lang.System');

// Assume we have an array of order items with their prices
var orderItems = [
    { name: "Laptop", price: 1000 },
    { name: "Mouse", price: 20 },
    { name: "Keyboard", price: 50 }
];

// Calculate the total order amount
var totalAmount = 0;
for (var i = 0; i < orderItems.length; i++) {
    totalAmount += orderItems[i].price;
}

// Apply a 10% discount if the total is over 1000
if (totalAmount > 1000) {
    totalAmount *= 0.9;  // 10% discount
}

// Set the total amount as a global variable
execution.setVariable("orderTotal", totalAmount);
var receivedValue = execution.getVariable('Time')
System.out.println("Order total calculated: $" + totalAmount+ '  Hour '+receivedValue);