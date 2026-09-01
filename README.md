# banking-management-system

Core - Sistema de Gestão Bancária

Description

O Core é um sistema de gestão bancária desenvolvido em Java, destinado à gestão de clientes e contas bancárias.

Nas operações que envolvam pagamentos a entidades externas, o Core será responsável pela gestão, validação e autorização da operação, sendo o processamento do pagamento realizado por um sistema externo através de comunicação HTTP.


Main Entities (Data Base)

- Cliente: representa os clientes registados no sistema. Contém informações como nome, NIF, email, telefone e estado do cliente.

- Conta: representa as contas bancárias pertencentes aos clientes. Cada conta terá informações como IBAN, tipo de conta, saldo, estado e respetivo titular.

- Movimento: representa as operações realizadas numa conta bancária, como depósitos, levantamentos, transferências, pagamentos ou outras movimentações.

- Transferência: representa uma transferência de dinheiro entre duas contas, identificando a conta de origem, conta de destino, valor, data e estado da operação.


Main Business Rules

- Validar os dados dos clientes e das contas antes da realização das operações.

- Garantir que apenas contas ativas podem realizar operações bancárias.

- Validar a existência de saldo suficiente antes de operações de débito.

- Atualizar corretamente o saldo da conta após cada operação.

- Registar os movimentos associados às operações realizadas.

- Validar as contas envolvidas antes da realização de transferências.

- Aplicar limites e restrições definidos para cada tipo de operação.

- Garantir a consistência dos dados caso uma operação não seja concluída com sucesso.

- Nos pagamentos externos, validar e autorizar a operação antes do seu envio, através de HTTP, para processamento por um sistema externo.