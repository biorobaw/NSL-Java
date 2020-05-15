package model;


public class Edge
{
    public int points_to_node;
    public int direction;
    public int steps;
    public boolean trace;

    public Edge(int k, int l, int i1, boolean tr)
    {
        direction = k;
        points_to_node = l;
        steps = i1;
        trace= tr;
    }
}