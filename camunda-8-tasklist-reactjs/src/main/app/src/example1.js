
const schema = {
    components: [
        {
            key: 'creditor',
            label: 'Creditor',
            type: 'textfield',
            validate: {
                required: true
            }
        }
    ]
};

const data = {
    creditor: 'John Doe Company'
};

export const example1 = {
    data: data,
    schema: schema
}
