// Exporting multiple functions
const getCurrentDate = () => new Date().toISOString();

const formatCurrency = (amount, currency = 'USD') => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: currency
  }).format(amount);
};

// Method 1: Exporting multiple items
//exports.getCurrentDate = getCurrentDate;
//exports.formatCurrency = formatCurrency;

// Method 2: Exporting an object with multiple properties
// module.exports = { getCurrentDate, formatCurrency };


class Logger {
  constructor(name) {
    this.name = name;
  }

  log(message) {
    console.log(`[${this.name}] ${message}`);
  }

  error(error) {
    console.error(`[${this.name}] ERROR:`, error.message);
  }
}

// Exporting a single class
module.exports = {
  getCurrentDate,
  formatCurrency,
  Logger
}