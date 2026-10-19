import { defineConfig } from "cypress";

export default defineConfig({
    e2e: {
        baseUrl: 'http://localhost:5173',
        specPattern: 'cypress/e2e/**/*.cy.{js,ts}', // ensures it finds your .cy.js tests
        supportFile: 'cypress/support/commands.js', // your custom commands
        setupNodeEvents(on, config) {
            // implement node event listeners here if needed
        },
    },
});
