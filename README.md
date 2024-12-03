# Sistema de Gerenciamento de models.Cliente
 Trabalho Prático II da disciplina de Técnicas de Programação Avançada [2024/2].
---
## Introdução
Este trabalho tem como objetivo a implementação de um sistema para gerenciar o cadastro de clientes de uma empresa global, com foco em grandes volumes de dados. As principais operações a serem realizadas são:

1. Listar Clientes em ordem alfabética.
2. Pesquisar Clientes.
3. Inserir um novo cliente.
4. Remover um cliente.

O sistema utilizará uma interface gráfica com o usuário (GUI) baseada em JFrames, que deverá ser implementada conforme o estilo gráfico do Windows. **A manipulação do conjunto de dados será realizada através de algoritmos de ordenação externa, para otimizar o uso de memória durante o processamento de grandes arquivos.** O trabalho deve ser feito tomando como linha de base o pacote CMS dentro do [git da matéria](https://github.com/mbarcosta/TPA), que contém as classes abaixo descritas.

## Descrição das Classes

- **Cliente**: Representa um cliente da empresa, com atributos como nome, sobrenome, telefone, endereço e credit score. Esta classe será a base para armazenar e gerenciar informações dos clientes.

- **GeradorDeArquivosDeClientes**: Classe responsável por gerar arquivos de clientes para testes, possibilitando a criação de um arquivo com até 10 milhões de registros. A geração dos dados será realizada com a biblioteca Java Faker.

- **ArquivoCliente**: Implementa a interface `interfaces.ArquivoSequencial` e permite a manipulação dos dados de clientes armazenados em arquivos binários. Essa classe oferece métodos para abrir, fechar, ler e escrever registros de clientes.

- **BufferDeClientes**: Implementa um buffer intermediário para leitura e escrita de dados dos clientes entre o arquivo em disco e a memória principal, permitindo operações eficientes sobre grandes volumes de dados.

- **ClienteGUI**: Classe responsável pela interface gráfica do sistema, que inclui uma tabela para visualização de clientes. A interface apresenta funcionalidades de busca, inserção e remoção de clientes, além de exibir clientes em ordem alfabética.

- **ClienteGUI2**: Variante da interface gráfica projetada para otimizar a exibição de grandes volumes de dados. Essa versão utiliza o `models.BufferDeClientes` para carregar registros em lotes conforme o usuário rola a tabela, minimizando o uso de memória.

## Diagrama de Classes
![img.png](images/img.png)

---
## Requisitos de Implementação

Para a implementação, devem ser utilizadas as classes disponíveis no repositório GitHub, [pacote `cms`](https://github.com/mbarcosta/TPA/tree/master/src/entity/cms). O sistema deve realizar a ordenação dos registros no arquivo utilizando um algoritmo de ordenação externa.

## Instruções de Entrega

O trabalho deve ser entregue em formato PDF com os seguintes itens:
- Capa
- Descrição do Problema e Equipe
- Arquitetura da aplicação com diagrama de classes
- Descrição dos principais algoritmos utilizados
- Imagens das telas, demonstrando as operações realizadas
- Instruções detalhadas de execução
- Link para o código-fonte (documentado e comentado)

## Bibliografia

- Biblioteca Java Faker para geração de dados aleatórios de clientes: [https://github.com/DiUS/java-faker](https://github.com/DiUS/java-faker)