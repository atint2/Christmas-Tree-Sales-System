package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
        switch (viewName) {
            case "TransactionChoiceView":
                return new TransactionChoiceView(model);
            case "ScoutSearchView":
                return new ScoutSearchView(model);
            case "EnterTreeInfoView":
                //return new EnterTreeInfoView();
				return null;
            default:
                return null;
        }
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