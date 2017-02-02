package zero.zd.zquestionnaire;


import java.util.ArrayList;

/**
 * Helper class for retrieving QnA for different topics.
 *
 * Note: temporary implementation
 */
public class QnAHelper {

    public static ArrayList<QnA> getSoftEngQnA() {
        ArrayList<QnA> qnaList = new ArrayList<>();
        qnaList.add(new QnA("The product that software professionals build then support over long term",
                "Software"));
        qnaList.add(new QnA("When executed provides desired features, function and performance",
                "Instructions"));
        qnaList.add(new QnA("Enables the programs to adequately store and manipulate information",
                "Data Structures"));
        qnaList.add(new QnA("Describes the operation and use of the programs", "Documentation"));
        qnaList.add(new QnA("Stand-alone systems that are marketed and sold to any customer who wishes to buy them",
                "Generic Products"));
        qnaList.add(new QnA("Software that is commissioned by a specific customer to meet their own needs",
                "Customized Products"));
        qnaList.add(new QnA("Is concerned with theories, methods and tools for professional software development",
                "Software Engineering"));
        qnaList.add(new QnA("Often dominate computer system costs. The costs of software on a PC are often greater than the hardware cost",
                "Software costs"));
        qnaList.add(new QnA("Is developed or engineered , it is not manufactured in the classical sense which has quality problem",
                "Software"));
        qnaList.add(new QnA("A software application such as compilers, editors, file management utilities",
                "System Software"));
        qnaList.add(new QnA("A stand-alone programs for specific needs", "Application Software"));
        qnaList.add(new QnA("Characterized by \"number crunching\" algorithms, such as automotive stress analysis, molecular biology, orbital dynamics etc.",
                "Engineering/Scientific Software"));
        qnaList.add(new QnA("Resides within a product or system", "Embedded Software"));
        qnaList.add(new QnA("Foccus on a limited marketplace to address mass consumer market",
                "Product-line Software"));
        qnaList.add(new QnA("Network centric software, more sophisticated computing environment is supported integrated with remote database and business application",
                "WebApps"));
        qnaList.add(new QnA("Software uses non-commercial algorithm to solve complex problems",
                "AI"));
        qnaList.add(new QnA("Pervasive, ubiquitous, distributed computing due to wireless networking",
                "Open world computing"));
        qnaList.add(new QnA("The web as a computing engine. How to architect simple and sophisticated applications to target end-users worldwide.",
                "Netsourcing"));
        qnaList.add(new QnA("\"free\" source code open to the computing community", "Open Source"));
        qnaList.add(new QnA("Is The establishment and use of sound engineering principles n order to obtain economically software that is reliable and works efficiently on real machines",
                "Software Engineering"));
        qnaList.add(new QnA("The application of a semantic disciplined, quantifiable approach to the development, operation, and maintenance of software",
                "Software Engineering"));
        qnaList.add(new QnA("Computer programs, data structures and associated documentation. Software products may be developed for a particular customer or may be developed for a general market.",
                "Software"));
        qnaList.add(new QnA("Characteristic of software that should be written in such a way so that it can evolve to meet the changing needs of customers. This is a critical attribute because software change is an inevitable requirement of a changing business environment.",
                "Maintainability"));
        qnaList.add(new QnA("Software dependability includes a range of characteristics including reliability, security and safety. Dependable software should not cause physical or economic damage in the event of system failure. Malicious users should not be  able to access or damage the system.",
                "Dependability and security"));
        qnaList.add(new QnA("Software should not make wasteful use of system resources such as memory and processor cycles. Efficiency therefore includes responsiveness, processing time, memory utilisation, etc.",
                "Efficiency"));
        qnaList.add(new QnA("Software must be acceptable to the type of users for which it is designed. This means that it must be understandable, usable and compatible with other systems that they use.",
                "Acceptability"));
        qnaList.add(new QnA("Provides automated or semi-automated support for the process and methods",
                "Tools"));
        qnaList.add(new QnA("Provides technical how-to’s for building software. It encompasses many tasks including communication, requirement analysis, design modeling, program construction, testing and support.",
                "Method"));
        qnaList.add(new QnA("The foundation defines a framework with activities for effective delivery of software engineering technology. Establish the context where products (model, data, report, and forms) are produced, milestone are established, quality is ensured and change is managed",
                "Process Layer"));
        qnaList.add(new QnA("Is a collection of activities, actions and tasks that are performed when some work product is to be created.",
                "Process"));
        qnaList.add(new QnA("Is to deliver software in a timely manner and with sufficient quality to satisfy those who have sponsored its creation and those who will use it.",
                "Purpose of Process"));
        qnaList.add(new QnA("Communicate with customer to understand objectives and gather requirements",
                "Communication"));
        qnaList.add(new QnA("Creates a “map” defines the work by describing the tasks, risks and resources, work products and work schedule.",
                "Planning"));
        qnaList.add(new QnA("Create a “sketch”, what it looks like architecturally, how the constituent parts fit together and other characteristics.",
                "Modelling"));
        qnaList.add(new QnA("Code generation and the testing.", "Construction"));
        qnaList.add(new QnA("Delivered to the customer who evaluates the products and provides feedback based on the evaluation.",
                "Deployment"));
        qnaList.add(new QnA("Assess progress against the plan and take actions to maintain the schedule. ",
                "Tracking and Control"));
        qnaList.add(new QnA("Assesses risks that may affect the outcome and quality.", "Risk management"));
        qnaList.add(new QnA("Defines and conduct activities to ensure quality.", "Software quality assurance"));
        qnaList.add(new QnA("assesses work products to uncover and remove errors before going to the next activity.",
                "Technical reviews"));
        qnaList.add(new QnA("define and collects process, project, and product measures to ensure stakeholder’s needs are met.",
                "Measurement"));
        qnaList.add(new QnA("manage the effects of change throughout the software process. ",
                "Software config mgmt."));
        qnaList.add(new QnA("defines criteria for work product reuse and establishes mechanism to achieve reusable components.",
                "Reusability management"));
        qnaList.add(new QnA("create work products such as models, documents, logs, forms and lists.",
                "Product Preparation and Production"));
        qnaList.add(new QnA("Models stress detailed definition, identification, and application of process activates and tasks.",
                "Prescriptive Process"));
        qnaList.add(new QnA("Emphasize project “agility” and follow a set of principles that lead to a more informal approach to software process.",
                "Agile process models "));

        return qnaList;
    }


}
