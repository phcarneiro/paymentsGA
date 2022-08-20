package br.ufg.inf.auditOP;

import java.util.ArrayList;
import java.util.Random;

public class Population {

    private Integer popSize;
    private final ArrayList<Member> popMembers;

    private Integer generation;

    public ArrayList<Member> getPopMembers() {
        return this.popMembers;
    }

    public Integer getGeneration() {
        return this.generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public void setPopSize(Integer popSize) {
        this.popSize = popSize;
    }

    public Population(int popSize, Bag bag) {
        this.popMembers = new ArrayList<Member>();
        for (int i = 0; i < popSize; i++) {
            Member member = new Member(bag.getListaPagamentos().size());
            member.evaluateMemberFitness(bag);
            this.popMembers.add(member);
        }
        this.popSize = popSize;
    }

    // Constructor default. Generation 0.
    public Population(Integer popSize, Bag bag) {
        this.popMembers = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            Member member = new Member(bag.getListaPagamentos().size());
            member.evaluateMemberFitness(bag);
            this.popMembers.add(member);
        }
        this.popSize = popSize;


    }

    // Constructor for building a new population on selected members
    public Population(ArrayList<Member> selectedMembers) {
        this.popMembers = new ArrayList<>();
        this.popMembers.addAll(selectedMembers);
        this.popSize = selectedMembers.size();
    }


    public Population onePointCrossover(Double crossoverRate) {
        // Here, we need to recombine members to generate offspring and add it to population
        Random rn = new Random();
        ArrayList<Member> offspringPopulation = new ArrayList<>();
        ArrayList<Member> parentPopulation = this.getPopMembers();
        for (int i = 0; i < parentPopulation.size() / 2; i++) {
            //Randomly looking for parents to apply 1 point crossover
            int parent1 = rn.nextInt(0, parentPopulation.size() - 1);
            int parent2 = rn.nextInt(0, parentPopulation.size() - 1);
            StringBuilder parentA = new StringBuilder(parentPopulation.get(parent1).getChromosome());
            StringBuilder parentB = new StringBuilder(parentPopulation.get(parent2).getChromosome());

            // Crossover rate check
            Double chanceToGenOffspring = rn.nextDouble(0.0, 1.0);
            if (chanceToGenOffspring <= crossoverRate) {
                int length = parentA.length();
                int cutPosition = rn.nextInt(0, length - 1);
                String sTailA = parentA.substring(cutPosition);
                String sTailB = parentB.substring(cutPosition);
                StringBuilder tailA = new StringBuilder(sTailA);
                StringBuilder tailB = new StringBuilder(sTailB);
                StringBuilder tailOffspringA1 = parentA.replace(cutPosition, parentA.length(), tailB.toString());
                StringBuilder tailOffspringB1 = parentB.replace(cutPosition, parentB.length(), tailA.toString());
                Member offspringA1 = new Member(tailOffspringA1);
                Member offspringB1 = new Member(tailOffspringB1);
                offspringPopulation.add(offspringA1);
                offspringPopulation.add(offspringB1);
            } else {
                // Out the Crossover rate
                Member offspringACopy = new Member(parentA);
                Member offspringBCopy = new Member(parentB);
                offspringPopulation.add(offspringACopy);
                offspringPopulation.add(offspringBCopy);
            }
        }

        return new Population(offspringPopulation);
    }

    // The array [0.3, 0.6, 0.1, 0.4, 0.8, 0.7, 0.3, 0.5, 0.3] of
    // random numbers and p = 0.5 were used to decide inheritance for this example.
    public Population uniformCrossover(Double crossoverRate) {
        Random rn = new Random();
        ArrayList<Member> offspringPopulation = new ArrayList<>();
        ArrayList<Double> inheritanceArray = new ArrayList<>();
        ArrayList<Member> parentPopulation = this.getPopMembers();
        for (int i = 0; i < 100; i++) {
            Double chanceToGenOffspring = rn.nextDouble(0.0, 1.0);
            inheritanceArray.add(chanceToGenOffspring);
        }
        for (int i = 0; i < parentPopulation.size() / 2; i++) {
            //Random search for parents to apply uniform crossover
            int parent1 = rn.nextInt(0, parentPopulation.size() - 1);
            int parent2 = rn.nextInt(0, parentPopulation.size() - 1);
            StringBuilder parentA = new StringBuilder(parentPopulation.get(parent1).getChromosome());
            StringBuilder parentB = new StringBuilder(parentPopulation.get(parent2).getChromosome());
            StringBuilder offspringA = new StringBuilder();
            StringBuilder offspringB = new StringBuilder();
            int index = 0;
            for (Double chanceToInherit : inheritanceArray) {
                if (chanceToInherit < crossoverRate) {
                    copyParentsGenes(parentA, parentB, offspringA, offspringB, index);
                } else {
                    crossoverGenes(parentA, parentB, offspringA, offspringB, index);
                }
                index++;
            }
            offspringPopulation.add(new Member(offspringA));
            offspringPopulation.add(new Member(offspringB));
        }
        return new Population(offspringPopulation);
    }

    private void crossoverGenes(StringBuilder parentA, StringBuilder parentB, StringBuilder offspringA, StringBuilder offspringB, int index) {
        offspringA.append(new String(String.valueOf(parentB.charAt(index))));
        offspringB.append(new String(String.valueOf(parentA.charAt(index))));
    }

    private void copyParentsGenes(StringBuilder parentA, StringBuilder parentB, StringBuilder offspringA, StringBuilder offspringB, int index) {
        offspringA.append(new String(String.valueOf(parentA.charAt(index))));
        offspringB.append(new String(String.valueOf(parentB.charAt(index))));
    }

    public void mutate(Double mutationRate) {
        Random rn = new Random();
        for (Member member : this.getPopMembers()) {
            for (int i = 0; i < member.getChromosome().length(); i++) {
                Double chanceToBitFlipGen = rn.nextDouble(0.0, 1.0);
                if (chanceToBitFlipGen <= mutationRate) {
                    // If it is inside mutation range interval then the bit at index i is flipped
                    String s = member.getChromosome().toString();
                    char[] chars = new char[s.length()];
                    for (int c = 0; c < s.length(); c++) {
                        if (c == i) {
                            // flip the bottom bit so 0 flips to 1 and 1 flips to 0
                            chars[c] = (char) (s.charAt(c) ^ 1);
                        } else {
                            chars[c] = s.charAt(c);
                        }
                    }
                    String flipped = new String(chars);
                    member.setChromosome(new StringBuilder(flipped));
                }
            }
        }
    }

    public void evaluatePopulation(Bag bag) {
        this.popSize = this.popMembers.size();
        for (Member member : this.popMembers) {
            member.evaluateMemberFitness(bag);
        }
    }

    public Member selectTheBestSoFar() {
        Member bestSoFar = null;
        Integer bestSoFarFitness = 0;
        for (Member candidate : this.getPopMembers()) {
            bestSoFar = candidate.getFitness() >= bestSoFarFitness ? candidate : bestSoFar;
            bestSoFarFitness = bestSoFar.getFitness();
        }
        return bestSoFar;
    }

    public void removeRandomlyOffspring() {
        Random rn = new Random();
        Integer removeIndex = rn.nextInt(0, this.getPopMembers().size() - 1);
        Member memberOff = this.getPopMembers().get(removeIndex);
        this.getPopMembers().remove(memberOff);
    }

    public Population selectMembersToFormNewPopulationThroughTournaments() {
        ArrayList<Member> selectedMembers = new ArrayList<>();
        Random rn = new Random();
        int count = 0;
        while (count < this.getPopMembers().size()) {
            int posHamilton = rn.nextInt(0, this.getPopMembers().size() - 1);
            int posVerstappen = rn.nextInt(0, this.getPopMembers().size() - 1);
            while (posHamilton == posVerstappen) {
                posHamilton = rn.nextInt(0, this.getPopMembers().size() - 1);
                posVerstappen = rn.nextInt(0, this.getPopMembers().size() - 1);
            }

            boolean winner = this.getPopMembers().get(posHamilton).getFitness() > this.getPopMembers().get(posVerstappen).getFitness() ? selectedMembers.add(this.getPopMembers().get(posHamilton)) : selectedMembers.add(this.getPopMembers().get(posVerstappen));
            count++;
        }

        return new Population(selectedMembers);
    }

    public void fixMembersThatDoesNotFit(Bag bag) {
        //Start removing items to fit into the weight limit
        // Begin to insert items to best score values fitting the weight limit
        for (Member member : this.getPopMembers()) {
            if (member.getCandidateFitsWeightLimit()) continue;
            member.removeItems(bag);
            member.putItems(bag);
        }

    }
}