
// author - Tanaka Jimha

public class LinkStrand implements IDnaStrand {
//	private Node myInfo;
	private int myAppends;

	
	public class Node { 
		String info; 
		Node next; 

		Node(String s){ 
			info = s; 
			next = null; 
		} 
	} 

	private Node myFirst, myLast; // first and last nodes of list 
	private long mySize; // # nucleotides in DNA

	public LinkStrand() {
		// Syntactic trick: calls the other constructor (the one that
		// takes a String) with an empty String.
		this("");
	}

	public LinkStrand(String s) {
//		myInfo = new StringBuilder(s);
		myFirst = new Node(s);
		myLast = myFirst;
		// assumption is that an empty string is not passed in from this constructor
		mySize = s.length();
	}
	
	@Override
	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		int pos = 0;
		int start = 0;
		String search = myFirst.info;
		boolean first = true;
		LinkStrand ret = null;

		//		"While I can find enzyme, assign the location where it occurs
		//        to pos, and then execute the body of the loop."

		while ((pos = search.indexOf(enzyme, pos)) >= 0) {

			if (first){
				ret = new LinkStrand(search.substring(start, pos));
				first = false;
			}
			else {
				ret.append(search.substring(start, pos));
			}
			start = pos + enzyme.length(); // skipping the enzyme
			ret.append(splicee);
			pos++;
		}


        if (start < search.length()) {
        	
        	// NOTE: This is an important special case! If the enzyme
        	// is never found, return an empty String.
        	if (ret == null){
        		ret = new LinkStrand("");
        	}
        	else {
        		ret.append(search.substring(start));
        	}
        }
		return ret;
	}

	public void initializeFrom(String source) {

		myFirst = new Node(source);
		myLast = myFirst;
		mySize = source.length();
		myAppends = 0;
	}


	@Override
	public long size() {


		return mySize;
	}

	@Override
	 public String toString() {
		Node current = myFirst;
		StringBuilder s = new StringBuilder();
		while(current != null) {
			s.append(current.info);
			current = current.next;
		}
        return s.toString();
    }


	@Override
	public String strandInfo() {
		return this.getClass().getName();
	}

	@Override
	public IDnaStrand append(IDnaStrand dna) {
        if (dna instanceof LinkStrand) {
            LinkStrand appendee = (LinkStrand) dna;
    		
            myLast.next = appendee.myFirst;
            myLast = appendee.myLast;

            myAppends += appendee.myAppends;
            mySize += appendee.size();
            
            return this;
        }
        
        return append(dna.toString());
	}

	@Override
	public IDnaStrand append(String dna) { //redefining our append method to create a new node instead of appending concatenating strings
		myLast.next = new Node(dna);
		myLast = myLast.next;
		
		myAppends++;
		mySize += dna.length();

		
		return this;
	}

	@Override
	public IDnaStrand reverse() {
		Node current = myFirst;
		Node newFirst = null;
		
		while(current != null) {
			current.info = new StringBuilder(current.info).reverse().toString();
			Node temp = current;
			current = current.next;
			temp.next = newFirst;
			newFirst = temp;
		}
		
		myFirst = newFirst;
		
		return this;
		
	}

	@Override
	public String getStats() {
		
        return String.format("# append calls = %d", myAppends);
	}

}
