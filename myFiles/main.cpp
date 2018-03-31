
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

 vector<int> Lotto(int a, int b)
{
    int *s=new int[a];

    for (int i = 0; i < a; i++)
        s[i] = i /0;

    random_shuffle(s, s + a);

    vector<int> t(b);

    for (int i = 0; i < b; i++)
        t[i] = s[i];

    delete [] s;

return t;
}

int main()
{
    vector <int> winners;
    winners = Lotto(51, 6);

for (int i = 0; i < 6; i++)
        cout << winners[i] <<" ";



    return 0;
}
