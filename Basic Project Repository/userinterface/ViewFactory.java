package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		 if(viewName.equals("TransactionChoiceView"))
		{
			return new TransactionChoiceView(model);
		}
		else if (viewName.equals("ScoutSearchView"))
		{
			return new ScoutSearchView(model);
		} else if (viewName.equals("EnterTreeInfoView"))
		 {
			return new EnterTreeInfoView();
		 }
		else
			return null;
	}


	/*
	public static Vector createVectorView(String viewName, IModel model)
	{
		if(viewName.equals("SOME VIEW NAME") == true)
		{
			//return [A NEW VECTOR VIEW OF THAT NAME TYPE]
		}
		else
			return null;
	}
	*/

}