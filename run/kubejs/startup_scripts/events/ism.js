TFCEvents.registerItemStackModifier(event => {
    event.withInput('kubejs:test', (stack, input) => {
        let test = input.orCreateTag
        stack.orCreateTag.merge(`{test:${test}}`);
        return stack;
    })
})
