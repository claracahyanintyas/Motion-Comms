import Chance from 'chance'
const username = chance.string();
const email = chance.email();
const password = chance.string();

const payoutMethod = {
    type: "BANK_ACCOUNT",
    providerName: "ING",
    accountName: "Account",
    accountNumber: "123456789"
};
const commission ={
    title : chance.string(),
    description : chance.string(),
    price : chance.floating({ min: 0, max: 10000 }),
    downPayment : chance.floating({ min: 0, max: 100 })
};

describe('Upload Commission E2E', () => {

    before(() => {
        // Ensure user exists (register)
        cy.register(username, email, password)
        cy.login(username, password)
        cy.createPayoutMethod(payoutMethod.type, payoutMethod.providerName, payoutMethod.accountName, payoutMethod.accountNumber)

    });

    it('should allow uploading a commission', () => {
        // Go to Upload page
        cy.visit('http://localhost:5173/upload-commission');
        cy.url().should('include', '/upload-commission')

        cy.get('input[name="title"]').type(commission.title);
        cy.get('textarea[name="description"]').type(commission.description);
        cy.get('input[name="price"]').type(commission.price.toString());
        cy.get('input[name="downPaymentPercentage"]').type(commission.downPayment.toString());

        // Submit the form
        cy.get('button[type="submit"]').click();

        // Wait for navigation to /commissions
        cy.url().should('include', '/commissions');

        // Optional: check that the new commission shows up in the list
        cy.contains(commission.title).should('exist');
        cy.contains(commission.price.toFixed(2)).should('exist');
    });
});
