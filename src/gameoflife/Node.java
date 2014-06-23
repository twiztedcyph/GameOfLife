
package gameoflife;

/**
 *
 * @author Cypher
 */
public class Node
{
    private Boolean state, changeOnNext;
    
    public Node(Boolean state)
    {
        this.state = state;
        changeOnNext = false;
    }
    
    public Boolean getState()
    {
        return this.state;
    }
    
    public void changeNextTurn()
    {
        changeOnNext = true;
    }
    
    public void change()
    {
        if(changeOnNext)
        {
            state = !state;
        }
        changeOnNext = false;
    }
    
    @Override
    public String toString()
    {
        if (state)
        {
            return "1";
        }
        return "0";
    }
}
