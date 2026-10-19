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

describe('Payout Method Creation Flow', () => {
    it('should create payout method successfully', () => {
        cy.register(username, email, password);
        cy.login(username, password);

        cy.visit("http://localhost:5173/payout-methods/create");

        cy.get("select").select(payoutMethod.type); // select type
        cy.get("input[name='providerName']").type(payoutMethod.providerName);
        cy.get("input[name='accountName']").type(payoutMethod.accountName);
        cy.get("input[name='accountNumber']").type(payoutMethod.accountNumber);
        cy.get("button[type='submit']").click();
        cy.url().should('include', '/users')
    })
})
describe('Payout Method Input not filled in', () => {
    it('should not create payout method', () => {

        cy.login(username, password);
        cy.visit("http://localhost:5173/payout-methods/create");

        cy.get("select").select(payoutMethod.type); // select type
        cy.get("input[name='providerName']").type(payoutMethod.providerName);
        cy.get("button[type='submit']").click();
        cy.url().should('include', '/create')
        cy.contains('Account name is required')
    })
})