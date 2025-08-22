// Import Java's System class for logging
var System = Java.type('java.lang.System');

// Retrieve the order total from the global variable
var orderTotal = execution.getVariable("orderTotal");

// Simulate a payment process
var paymentSuccessful = (Math.random() < 0.8);  // 80% chance of success

if (paymentSuccessful) {
    System.out.println("Payment of $" + orderTotal + " processed successfully.");
    execution.setVariable("paymentStatus", "SUCCESS");
} else {
    System.out.println("Payment of $" + orderTotal + " failed. Please try again.");
    execution.setVariable("paymentStatus", "FAILED");
}