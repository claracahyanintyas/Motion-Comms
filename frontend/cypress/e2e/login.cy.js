import Chance from 'chance'
const username = chance.string();
const email = chance.email();
const password = chance.string();

describe('Login Flow', () => {
    it('should login successfully', () => {
        cy.register(username, email, password)
        cy.url().should('include', '/login')
        cy.visit('http://localhost:5173/login')
        cy.get('[data-cy="usernameOrEmail"]').type(username)
        cy.get('[data-cy="password"]').type(password)
        cy.get('[data-cy="submit-form"]').click()
        cy.contains('logged in')
    })
})
