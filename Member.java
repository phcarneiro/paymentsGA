package br.ufg.inf.auditOP;

import br.ufg.inf.auditOP.Bag;

import java.util.Random;

public class Member {
    private Integer fitness; // in this case, it is the sum memberSumValues and memberSumWeights or zero - does not fit the weight limit
    private StringBuilder chromosome;
    private Integer memberSumValues;
    private Boolean candidateFitsWeightLimit;

    private Integer sumWeightsN1;

    public Boolean getCandidateFitsWeightLimit() {
        return candidateFitsWeightLimit;
    }

    public void setCandidateFitsWeightLimit(Boolean candidateFitsWeightLimit) {
        this.candidateFitsWeightLimit = candidateFitsWeightLimit;
    }

    public Integer getSumWeightsN1() {
        return sumWeightsN1;
    }

    public void setSumWeightsN1(Integer sumWeightsN1) {
        this.sumWeightsN1 = sumWeightsN1;
    }

    public Integer getMemberSumValues() {
        return memberSumValues;
    }

    public void setMemberSumValues(Integer memberSumValues) {
        this.memberSumValues = memberSumValues;
    }

    public Member(StringBuilder builder) {
        this.chromosome = builder;
        this.fitness = 0;
        this.initiateArrays();
    }

    public StringBuilder getChromosome() {
        return chromosome;
    }

    public void setChromosome(StringBuilder chromosome) {
        this.chromosome = chromosome;
    }

    public Integer getFitness() {
        return this.fitness;
    }

    public void setFitness(Integer fitness) {
        this.fitness = fitness;
    }

    public Member(Integer size) {
        Random rn = new Random();
        StringBuilder chromosomeStr = new StringBuilder();
        int contador = 0;
        while (contador < size) {
            String gen = (rn.nextBoolean() == Boolean.FALSE) ? "0" : "1";
            chromosomeStr.append(gen);
            contador++;
        }
        this.chromosome = chromosomeStr;

        this.fitness = 0;
        this.initiateArrays();
    }

    private void initiateArrays() {
        setSumWeightsN1(0);
    }

    // Here I need to decode individual chromosome into a candidate solution for each knapsack problem.
    public void evaluateMemberFitness(Bag bag) {
        Integer sumValues = 0;

        Integer sumWeightsN1 = 0;

        // Identifying solution candidates that appears on the randomly generated member
        // Calculating member p(m) weights
        for (int i = 0; i < getChromosome().length(); i++) {
            if (getChromosome().charAt(i) == '1') {
                // This combination of values and weights will be on the bag.
                sumValues += bag.getListaPagamentos().get(i).getSumFatosRelevantes();

                sumWeightsN1 = sumWeightsN1 + bag.getListaPagamentos().get(i).getCusto().horas;
            }
        }
        setMemberSumValues(sumValues);
        setSumWeightsN1(sumWeightsN1);
        if (getSumWeightsN1() > Bag.LIMITE_CUSTO) {
            setCandidateFitsWeightLimit(Boolean.FALSE);
        } else {
            setCandidateFitsWeightLimit(Boolean.TRUE);
        }
        // the member that does not fit the weight limit does not get a fitness score
        setFitness(getCandidateFitsWeightLimit() ? getMemberSumValues() : 0);
    }

    public void removeItems(Bag bag) {
        Random rn = new Random();
        while (!this.getCandidateFitsWeightLimit()) {
            int randomPosition = rn.nextInt(0, this.getChromosome().length());
            while (this.getChromosome().charAt(randomPosition) == '0')
                randomPosition = rn.nextInt(0, this.getChromosome().length());
            bitFlip(randomPosition);
            this.evaluateMemberFitness(bag);
        }
    }

    public void putItems(Bag bag) {
        int credits = 0;
        for (int c = 0; c < this.getChromosome().length(); c++) {
            if (this.getChromosome().charAt(c) == '0') credits++;
        }
        Random rn = new Random();
        int index = 0;
        while (index < credits) {
            int randomPosition = rn.nextInt(0, this.getChromosome().length());
            while (this.getChromosome().charAt(randomPosition) == '1')
                randomPosition = rn.nextInt(0, this.getChromosome().length());
            String flipped = bitFlipForTestTheSolutionCandidate(randomPosition);
            Member candidate = new Member(new StringBuilder(flipped));
            candidate.evaluateMemberFitness(bag);
            if (candidate.getCandidateFitsWeightLimit()) {
                updateMember(candidate);
            }
            index++;
        }
    }

    private String bitFlipForTestTheSolutionCandidate(int randomPosition) {
        String s = String.valueOf(this.getChromosome());
        char[] chars = new char[s.length()];
        for (int c = 0; c < s.length(); c++) {
            if (c == randomPosition) {
                // flip the bottom bit so 0 flips to 1 and 1 flips to 0
                chars[c] = (char) (s.charAt(c) ^ 1);
            } else {
                chars[c] = s.charAt(c);
            }
        }
        String flipped = new String(chars);
        return flipped;
    }

    private void updateMember(Member newCandidate) {
        this.setChromosome(newCandidate.getChromosome());
        this.setMemberSumValues(newCandidate.getMemberSumValues());
        this.setSumWeightsN1(newCandidate.getSumWeightsN1());
        this.setFitness(newCandidate.getFitness());
        this.setCandidateFitsWeightLimit(newCandidate.getCandidateFitsWeightLimit());
    }

    private void bitFlip(int randomPosition) {
        String s = this.getChromosome().toString();
        char[] chars = new char[s.length()];
        for (int c = 0; c < s.length(); c++) {
            if (c == randomPosition) {
                // flip the bottom bit so 0 flips to 1 and 1 flips to 0
                chars[c] = (char) (s.charAt(c) ^ 1);
            } else {
                chars[c] = s.charAt(c);
            }
        }
        String flipped = new String(chars);
        this.setChromosome(new StringBuilder(flipped));
    }
}